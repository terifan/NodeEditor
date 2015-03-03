package org.terifan.ui.relationeditor;

import java.util.UUID;
import javax.swing.JComponent;


public abstract class RelationItem extends JComponent
{
	abstract UUID getIdentity();
}
