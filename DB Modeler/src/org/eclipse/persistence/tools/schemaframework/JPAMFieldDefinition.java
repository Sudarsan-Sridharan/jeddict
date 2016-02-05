/**
 * Copyright [2016] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.eclipse.persistence.tools.schemaframework;

import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.netbeans.db.modeler.spec.DBColumn;
import org.netbeans.db.modeler.spec.DBInverseJoinColumn;
import org.netbeans.db.modeler.spec.DBJoinColumn;
import org.netbeans.db.modeler.spec.DBTable;
import org.netbeans.jpa.modeler.spec.ElementCollection;
import org.netbeans.jpa.modeler.spec.extend.Attribute;
import org.netbeans.jpa.modeler.spec.extend.RelationAttribute;
import org.netbeans.modeler.core.NBModelerUtil;

public class JPAMFieldDefinition extends FieldDefinition {

    private Attribute attribute;
    private boolean inverse;
    private boolean foriegnKey;

//    public JPAMFieldDefinition() {
//
//    }
//    public JPAMFieldDefinition(Attribute attribute, boolean inverse) {
//        this.attribute = attribute;
//        this.inverse = inverse;
//    }

    public JPAMFieldDefinition(Attribute attribute, boolean inverse, boolean foriegnKey) {
        this.attribute = attribute;
        this.inverse = inverse;
        this.foriegnKey = foriegnKey;
    }
    

    public JPAMFieldDefinition(Attribute attribute) {
        this.attribute = attribute;
    }

    public JPAMFieldDefinition(Attribute attribute, String name, Class type) {
        super(name, type);
        this.attribute = attribute;
    }

    public JPAMFieldDefinition(Attribute attribute, String name, Class type, int size) {
        super(name, type, size);
        this.attribute = attribute;
    }

    public JPAMFieldDefinition(Attribute attribute, String name, Class type, int size, int subSize) {
        super(name, type, size, subSize);
        this.attribute = attribute;
    }

    public JPAMFieldDefinition(Attribute attribute, String name, String typeName) {
        super(name, typeName);
        this.attribute = attribute;
    }

    /**
     * INTERNAL: Append the database field definition string to the table
     * creation statement.
     *
     * @param writer Target writer where to write field definition string.
     * @param session Current session context.
     * @param table Database table being processed.
     * @throws ValidationException When invalid or inconsistent data were found.
     */
    public void appendDBString(final DBTable table, final AbstractSession session,
            final JPAMTableDefinition tableDef) throws ValidationException {

        DBColumn column = null;

        if (foriegnKey) {
            if (attribute instanceof RelationAttribute) {
                if (inverse) {//((RelationAttribute)attribute).isOwner()
                    column = new DBInverseJoinColumn(name, (RelationAttribute) attribute);
                } else {
                    column = new DBJoinColumn(name,  attribute);
                }
            } else if (attribute instanceof ElementCollection) {
                column = new DBJoinColumn(name, attribute);
            }
        } else {
            column = new DBColumn(name, attribute);
        }

        column.setId(NBModelerUtil.getAutoGeneratedStringId());

        if (getTypeDefinition() != null) { //apply user-defined complete type definition
            //TODO
        } else {
            final FieldTypeDefinition fieldType = type != null ? session.getPlatform().getFieldTypeDefinition(type) : new FieldTypeDefinition(typeName);
            if (fieldType == null) {
                throw ValidationException.javaTypeIsNotAValidDatabaseType(type);
            }
            column.setDataType(fieldType.getName());

            if ((fieldType.isSizeAllowed()) && ((this.getSize() != 0) || (fieldType.isSizeRequired()))) {
                if (this.getSize() == 0) {
                    column.setSize(fieldType.getDefaultSize());
                } else {
                    column.setSize(this.getSize());
                }
                if (this.getSubSize() != 0) {
                    column.setSubSize(this.getSubSize());
                } else if (fieldType.getDefaultSubSize() != 0) {
                    column.setSubSize(fieldType.getDefaultSubSize());
                }
            }

            if (shouldAllowNull && fieldType.shouldAllowNull()) {
                // NULL
            } else {
                //NOT NULL
            }
        }
        column.setPrimaryKey(isPrimaryKey && session.getPlatform().supportsPrimaryKeyConstraint());

        table.addColumn(column);

    }

}
