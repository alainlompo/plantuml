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
package net.sourceforge.plantuml.project3;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;

public class NaturalCommandAnd extends SingleLineCommand2<GanttDiagram> {

	private final SubjectPattern subjectPattern;
	private final VerbPattern verbPattern1;
	private final ComplementPattern complementPattern1;
	private final VerbPattern verbPattern2;
	private final ComplementPattern complementPattern2;

	private NaturalCommandAnd(RegexConcat pattern, SubjectPattern subject, VerbPattern verb1,
			ComplementPattern complement1, VerbPattern verb2, ComplementPattern complement2) {
		super(pattern);
		this.subjectPattern = subject;
		this.verbPattern1 = verb1;
		this.complementPattern1 = complement1;
		this.verbPattern2 = verb2;
		this.complementPattern2 = complement2;
	}

	@Override
	public String toString() {
		return subjectPattern.toString() + " " + verbPattern1.toString() + " " + complementPattern1.toString()
				+ " and " + verbPattern2.toString() + " " + complementPattern2.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram system, RegexResult arg) {
		final Subject subject = subjectPattern.getSubject(system, arg);
		final Verb verb1 = verbPattern1.getVerb(system, arg);
		final Failable<Complement> complement1 = complementPattern1.getComplement(system, arg, "1");
		if (complement1.isFail()) {
			return CommandExecutionResult.error(complement1.getError());
		}
		final CommandExecutionResult result1 = verb1.execute(subject, complement1.get());
		if (result1.isOk() == false) {
			return result1;
		}
		final Verb verb2 = verbPattern2.getVerb(system, arg);
		final Failable<Complement> complement2 = complementPattern2.getComplement(system, arg, "2");
		if (complement2.isFail()) {
			return CommandExecutionResult.error(complement2.getError());
		}
		return verb2.execute(subject, complement2.get());
	}

	public static Command create(SubjectPattern subject, VerbPattern verb1, ComplementPattern complement1,
			VerbPattern verb2, ComplementPattern complement2) {
		final RegexConcat pattern = new RegexConcat(//
				new RegexLeaf("^"), //
				subject.toRegex(), //
				new RegexLeaf("[%s]+"), //
				verb1.toRegex(), //
				new RegexLeaf("[%s]+"), //
				complement1.toRegex("1"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("and"), //
				new RegexLeaf("[%s]+"), //
				verb2.toRegex(), //
				new RegexLeaf("[%s]+"), //
				complement2.toRegex("2"), //
				new RegexLeaf("$"));
		return new NaturalCommandAnd(pattern, subject, verb1, complement1, verb2, complement2);
	}
}
