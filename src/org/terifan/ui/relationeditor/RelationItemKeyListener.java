package org.terifan.ui.relationeditor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import org.terifan.util.log.Log;


public class RelationItemKeyListener extends KeyAdapter
{
	@Override
	public void keyPressed(KeyEvent aEvent)
	{
		try
		{
			RelationBox box = (RelationBox)SwingUtilities.getAncestorOfClass(RelationBox.class, aEvent.getComponent());
			RelationItem item = box.getItemByComponent(aEvent.getComponent());

			if (aEvent.getKeyCode() == KeyEvent.VK_F2)
			{
				box.startEditItem(item);
			}
			if (aEvent.getKeyCode() == KeyEvent.VK_DELETE)
			{
				((AbstractRelationBox)box).removeItem(item);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(Log.out);
		}
	}
}
