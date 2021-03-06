/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ChangeState implements Comparable<ChangeState> {

	private final TimeTick when;
	private final String state;
	private final String comment;
	private final Colors colors;

	public ChangeState(TimeTick when, String state, String comment, Colors colors) {
		this.when = when;
		this.state = state;
		this.comment = comment;
		this.colors = colors;
	}

	public int compareTo(ChangeState other) {
		return this.when.compareTo(other.when);
	}

	public final TimeTick getWhen() {
		return when;
	}

	public final String getState() {
		return state;
	}

	public String getComment() {
		return comment;
	}

	private final HtmlColor getBackColor() {
		if (colors == null || colors.getColor(ColorType.BACK) == null) {
			return HtmlColorUtils.COL_D7E0F2;
		}
		return colors.getColor(ColorType.BACK);
	}

	private final HtmlColor getLineColor() {
		if (colors == null || colors.getColor(ColorType.LINE) == null) {
			return HtmlColorUtils.COL_038048;
		}
		return colors.getColor(ColorType.LINE);
	}

	public SymbolContext getContext() {
		return new SymbolContext(getBackColor(), getLineColor()).withStroke(new UStroke(1.5));
	}

	public final boolean isHidden() {
		return state.length() == 0;
	}

}
