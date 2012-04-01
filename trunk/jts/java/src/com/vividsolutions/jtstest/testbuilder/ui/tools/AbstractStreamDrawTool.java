/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */
package com.vividsolutions.jtstest.testbuilder.ui.tools;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jtstest.testbuilder.AppCursors;
import com.vividsolutions.jtstest.testbuilder.model.GeometryType;

/**
 * @version 1.7
 */
public abstract class AbstractStreamDrawTool extends LineBandTool {

	protected AbstractStreamDrawTool() {
	  super();
    //cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    cursor = AppCursors.DRAW_GEOM;

	}

	protected abstract int getGeometryType();

  protected void mouseLocationChanged(MouseEvent e) {
    try {
      if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) 
          == InputEvent.BUTTON1_DOWN_MASK) {
        Coordinate newCoord = toModelCoordinate(e.getPoint());
        if (newCoord.distance(lastCoordinate()) < gridSize())
          return;
        //add(toModelSnapped(e.getPoint()));
        add(newCoord);
      }

      tentativeCoordinate = toModelSnapped(e.getPoint());
      redrawIndicator();
    } catch (Throwable t) {
    }
  }

	public void mouseClicked(MouseEvent e) {
		setBandType();
		super.mouseClicked(e);
	}

	protected void bandFinished() throws Exception {
		setType();
		geomModel().addComponent(getCoordinates());
		panel().updateGeom();
	}

	private void setType() {
		if (panel().getModel() == null)
			return;
		panel().getGeomModel().setGeometryType(getGeometryType());
	}

	private void setBandType() {
		int geomType = getGeometryType();
		setCloseRing(geomType == GeometryType.POLYGON);
	}
}