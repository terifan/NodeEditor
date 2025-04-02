package org.terifan.nodeeditor.util;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * An executor using N threads to execute tasks submitted to it. The close method will block until all submitted tasks have finished
 * executing.
 *
 * <pre>
 * try (FixedThreadExecutor executor = new FixedThreadExecutor(1f))
 * {
 *    executor.submit(() -> {System.out.println("doing something");});
 * }
 * </pre>
 */
public class FixedThreadExecutor<T> implements AutoCloseable
{
	private final Object LOCK = new Object();
	private final LinkedBlockingQueue mBlockingQueue;
	private final int mThreads;
	private int mCapacity;
	private ExecutorService mExecutorService;


	/**
	 * Create a new executor
	 *
	 * @param aNumThreads a positive number equals number of threads to use, zero or a negative number results in total available processors
	 * minus provided number.
	 */
	public FixedThreadExecutor(int aNumThreads)
	{
		if (aNumThreads > 0)
		{
			mThreads = aNumThreads;
		}
		else
		{
			mThreads = Math.max(1, getAvailableProcessors() + aNumThreads);
		}

		mBlockingQueue = withBlockingQueue();
	}


	/**
	 * Create a new executor
	 *
	 * @param aThreads number of threads expressed as a number between 0 and 1 out of total available CPUs
	 */
	public FixedThreadExecutor(float aThreads)
	{
		if (aThreads < 0 || aThreads > 1)
		{
			throw new IllegalArgumentException();
		}

		int cpu = getAvailableProcessors();
		mThreads = Math.max(1, Math.min(cpu, (int)Math.round(cpu * aThreads)));
		mBlockingQueue = withBlockingQueue();
	}


	/**
	 * Shutdown the executor immediately.
	 */
	public void shutdown()
	{
		if (mExecutorService != null)
		{
			ExecutorService es = mExecutorService;
			mExecutorService = null;
			es.shutdown();

			try
			{
				es.awaitTermination(1, TimeUnit.DAYS);
			}
			catch (InterruptedException e)
			{
			}
		}
	}


	/**
	 * Shutdown the executor immediately.
	 */
	public List<Runnable> shutdownNow()
	{
		if (mExecutorService != null)
		{
			ExecutorService es = mExecutorService;
			mExecutorService = null;
			return es.shutdownNow();
		}

		return new ArrayList<>();
	}


	/**
	 * Execute a task that don't generate a response. This method may block if the queue size exceeds the limit.
	 */
	public Future<T> submit(RunnableTask aRunnable)
	{
		return handle(aRunnable);
	}


	/**
	 * Execute a task that generate a response. This method may block if the queue size exceeds the limit.
	 */
	public Future<T> call(CallableTask aRunnable)
	{
		return handle(aRunnable);
	}


	private Future<T> handle(Object aRunnable)
	{
		synchronized (LOCK)
		{
			try
			{
				while (mCapacity > 0 && mBlockingQueue.size() >= mCapacity)
				{
					LOCK.wait(1000);
				}
			}
			catch (InterruptedException e)
			{
			}
		}

		ExecutorService es = init();

		switch (aRunnable)
		{
			case RunnableTask v -> {
				return (Future<T>)es.submit(() ->
				{
					try
					{
						v.run();
						synchronized (LOCK)
						{
							LOCK.notify();
						}
					}
					catch (Exception | Error e)
					{
						onException(e);
					}
				});
			}
			case CallableTask v -> {
				return (Future<T>)es.submit(() ->
				{
					try
					{
						Object r = v.run();
						synchronized (LOCK)
						{
							LOCK.notify();
						}
						return r;
					}
					catch (Exception | Error e)
					{
						onException(e);
						return null;
					}
				});
			}
			default ->
			{
				return null;
			}
		}
	}


	/**
	 * Blocks until all tasks finished executing. Repeated calls will be ignored and cause no problem. A closed FixedThreadExecutor is
	 * restarted when a task is submitted to it.
	 */
	@Override
	public void close()
	{
		close(Long.MAX_VALUE);
	}


	private synchronized boolean close(long aWaitMillis)
	{
		if (mExecutorService != null)
		{
			ExecutorService es = mExecutorService;
			mExecutorService = null;

			try
			{
				es.shutdown();
				es.awaitTermination(aWaitMillis, TimeUnit.MILLISECONDS);
				return false;
			}
			catch (InterruptedException e)
			{
				return true;
			}
		}

		return false;
	}


	private synchronized ExecutorService init()
	{
		if (mExecutorService == null)
		{
			mCapacity = withCapacity();
			mExecutorService = new ThreadPoolExecutor(mThreads, mThreads, 0L, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>)mBlockingQueue, aRunnable ->
			{
				Thread thread = Executors.defaultThreadFactory().newThread(aRunnable);
				thread.setDaemon(withDaemon());
				return thread;
			})
			{
				@Override
				protected void afterExecute(Runnable aRunnable, Throwable aThrowable)
				{
					super.afterExecute(aRunnable, aThrowable);

					synchronized (LOCK)
					{
						LOCK.notify();
					}

					if (aThrowable != null)
					{
						onException(aThrowable);
					}
					else
					{
						try
						{
							Future f = (Future)aRunnable;
							if (f.isCancelled())
							{
								onCancelled(f);
							}
							else
							{
								onCompletion(f);
							}
						}
						catch (Exception | Error e)
						{
							onException(e);
						}
					}
				}
			};
		}

		return mExecutorService;
	}


	protected int getAvailableProcessors()
	{
		return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
	}


	/**
	 * Override to return 'true' for daemon threads.
	 *
	 * @return false by default, override and return true to make executing threads be daemon.
	 */
	protected boolean withDaemon()
	{
		return false;
	}


	/**
	 * Override to return a capacity.
	 *
	 * @return unlimited (zero) by default, override to return a capacity of the blocking queue before it start to block adding new tasks.
	 */
	protected int withCapacity()
	{
		return 0;
	}


	/**
	 * Override to return custom implementations.
	 *
	 * @return a new LinkedBlockingQueue instance.
	 */
	protected LinkedBlockingQueue<T> withBlockingQueue()
	{
		return new LinkedBlockingQueue<>();
	}


	/**
	 * Override to do something when an error occurs.
	 */
	protected void onException(Throwable aThrowable)
	{
	}


	/**
	 * Override to do something immediately when a task is completed.
	 */
	protected void onCompletion(Future aFuture) throws Exception
	{
	}


	/**
	 * Override to do something immediately when a task is cancelled.
	 */
	protected void onCancelled(Future aFuture) throws Exception
	{
	}


	@FunctionalInterface
	public interface RunnableTask
	{
		void run() throws Exception;
	}


	@FunctionalInterface
	public interface CallableTask<U>
	{
		U run() throws Exception;
	}


//	public static void main(String... args)
//	{
//		try
//		{
//			try (FixedThreadExecutor executor = new FixedThreadExecutor(2)
//			{
//				@Override
//				protected void onException(Throwable aThrowable)
//				{
//					aThrowable.printStackTrace(System.out);
//				}
//				@Override
//				protected void onCompletion(Future f) throws Exception
//				{
//					System.out.println("completed " + f.get());
//				}
//				@Override
//				protected void onCancelled(Future f) throws Exception
//				{
//					System.out.println("cancelled " + f);
//				}
//				@Override
//				protected boolean withDaemon()
//				{
//					return true;
//				}
//				@Override
//				protected int withCapacity()
//				{
//					return 20;
//				}
//			})
//			{
//				ArrayList<Future> futures = new ArrayList<>();
//				for (int i = 0; i < 100; i++)
//				{
//					int j = i;
//					futures.add(executor.submit(() ->
//					{
//						System.out.println("test " + j);
//					}));
//					futures.add(executor.call(() ->
//					{
//						System.out.println("test " + j);
//						return "result " + j;
//					}));
//				}
//				futures.get(197).cancel(true);
//
//				List missed = executor.shutdownNow();
//
//				for (Future f : futures)
//				{
//					if (f.isDone() && !f.isCancelled())
//					{
//						System.out.println("finished " + f.get());
//					}
//				}
//
//				System.out.println(missed.size() + " missed");
//			}
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace(System.out);
//		}
//	}
}
