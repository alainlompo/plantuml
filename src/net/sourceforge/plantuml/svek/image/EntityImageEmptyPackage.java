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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Cluster;
import net.sourceforge.plantuml.svek.ClusterDecoration;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageEmptyPackage extends AbstractEntityImage {

	private final TextBlock desc;
	private final static int MARGIN = 10;
	private final HtmlColor specificBackColor;
	private final ISkinParam skinParam;
	private final Stereotype stereotype;
	private final TextBlock stereoBlock;
	private final Url url;

	public EntityImageEmptyPackage(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);
		this.skinParam = skinParam;
		this.specificBackColor = entity.getColors(skinParam).getColor(ColorType.BACK);
		this.stereotype = entity.getStereotype();
		this.desc = entity.getDisplay().create(new FontConfiguration(getSkinParam(), FontParam.PACKAGE, stereotype),
				HorizontalAlignment.CENTER, skinParam);
		this.url = entity.getUrl99();

		if (stereotype == null || stereotype.getLabel(false) == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false) {
			stereoBlock = TextBlockUtils.empty(0, 0);
		} else {
			stereoBlock = TextBlockUtils.withMargin(
					Display.create(stereotype.getLabels(skinParam.useGuillemet())).create(
							new FontConfiguration(getSkinParam(), FontParam.PACKAGE_STEREOTYPE, stereotype),
							HorizontalAlignment.CENTER, skinParam), 1, 0);
		}

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);
		Dimension2D dim = TextBlockUtils.mergeTB(desc, stereoBlock, HorizontalAlignment.LEFT).calculateDimension(
				stringBounder);
		dim = Dimension2DDouble.atLeast(dim, 0, 2 * dimDesc.getHeight());
		return Dimension2DDouble.delta(dim, MARGIN * 2, MARGIN * 2);
	}

	private UStroke getStroke() {
		UStroke stroke = getSkinParam().getThickness(LineParam.packageBorder, getStereo());
		if (stroke == null) {
			stroke = new UStroke(2.0);
		}
		return stroke;
	}

	final public void drawU(UGraphic ug) {
		if (url != null) {
			ug.startUrl(url);
		}

		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();

		final HtmlColor back = Cluster.getBackColor(specificBackColor, skinParam, stereotype);
		final double roundCorner = 0;

		final ClusterDecoration decoration = new ClusterDecoration(getSkinParam().getPackageStyle(), null, desc,
				stereoBlock, 0, 0, widthTotal, heightTotal, getStroke());

		decoration.drawU(ug, back, SkinParamUtils.getColor(getSkinParam(), ColorParam.packageBorder, getStereo()),
				getSkinParam().shadowing(), roundCorner);

		if (url != null) {
			ug.closeAction();
		}

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
