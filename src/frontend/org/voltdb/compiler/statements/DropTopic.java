/* This file is part of VoltDB.
 * Copyright (C) 2019 VoltDB Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.voltdb.compiler.statements;

import java.util.regex.Matcher;

import org.hsqldb_voltpatches.VoltXMLElement;
import org.voltdb.catalog.CatalogMap;
import org.voltdb.catalog.Database;
import org.voltdb.catalog.Topic;
import org.voltdb.common.Constants;
import org.voltdb.compiler.DDLCompiler;
import org.voltdb.compiler.DDLCompiler.DDLStatement;
import org.voltdb.compiler.DDLCompiler.StatementProcessor;
import org.voltdb.compiler.VoltCompiler.DdlProceduresToLoad;
import org.voltdb.compiler.VoltCompiler.VoltCompilerException;
import org.voltdb.parser.SQLParser;

public class DropTopic extends StatementProcessor {

    public DropTopic(DDLCompiler ddlCompiler) {
        super(ddlCompiler);
    }

    @Override
    protected boolean processStatement(DDLStatement ddlStatement, Database db, DdlProceduresToLoad whichProcs)
            throws VoltCompilerException {
        Matcher matcher = SQLParser.matchDropTopic(ddlStatement.statement);
        if (!matcher.matches()) {
            return false;
        }

        String name = matcher.group("name");
        CatalogMap<Topic> topics = db.getTopics();
        if (topics.get(name) == null) {
            if (matcher.group("ifExists") == null) {
                throw m_compiler.new VoltCompilerException(
                        String.format("Topic name \"%s\" in DROP TOPIC statement does not exist.", name));
            }
        } else {
            topics.delete(name);
            VoltXMLElement tableXML = m_schema.findChild("table", name.toUpperCase());
            if (tableXML != null) {
                tableXML.attributes.remove("topicName");
                tableXML.attributes.put("export", Constants.CONNECTORLESS_STREAM_TARGET_NAME);
            }
        }
        return true;
    }
}
