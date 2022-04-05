
package org.mulesoft.schema.mule.db;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.core.AbstractExtensionType;
import org.mulesoft.schema.mule.core.AnnotationsType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.db package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Select_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "select");
    private final static QName _Update_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "update");
    private final static QName _Delete_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "delete");
    private final static QName _Insert_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "insert");
    private final static QName _ExecuteDdl_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "execute-ddl");
    private final static QName _BulkExecute_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "bulk-execute");
    private final static QName _StoredProcedure_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "stored-procedure");
    private final static QName _AbstractQueryResultSetHandler_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "abstractQueryResultSetHandler");
    private final static QName _TemplateQuery_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "template-query");
    private final static QName _AbstractConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "abstract-config");
    private final static QName _ConnectionProperties_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "connection-properties");
    private final static QName _DataTypes_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "data-types");
    private final static QName _PoolingProfile_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "pooling-profile");
    private final static QName _GenericConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "generic-config");
    private final static QName _DerbyConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "derby-config");
    private final static QName _OracleConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "oracle-config");
    private final static QName _MysqlConfig_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "mysql-config");
    private final static QName _AbstractDbMixedContentMessageProcessorTypeAnnotations_QNAME = new QName("http://www.mulesoft.org/schema/mule/core", "annotations");
    private final static QName _TemplateSqlDefinitionTypeParameterizedQuery_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "parameterized-query");
    private final static QName _TemplateSqlDefinitionTypeInParam_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "in-param");
    private final static QName _TemplateSqlDefinitionTypeDynamicQuery_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "dynamic-query");
    private final static QName _TemplateSqlDefinitionTypeTemplateQueryRef_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "template-query-ref");
    private final static QName _ExecuteStoredProcedureMessageProcessorTypeOutParam_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "out-param");
    private final static QName _ExecuteStoredProcedureMessageProcessorTypeInoutParam_QNAME = new QName("http://www.mulesoft.org/schema/mule/db", "inout-param");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.db
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SelectMessageProcessorType }
     * 
     */
    public SelectMessageProcessorType createSelectMessageProcessorType() {
        return new SelectMessageProcessorType();
    }

    /**
     * Create an instance of {@link UpdateMessageProcessorType }
     * 
     */
    public UpdateMessageProcessorType createUpdateMessageProcessorType() {
        return new UpdateMessageProcessorType();
    }

    /**
     * Create an instance of {@link InsertMessageProcessorType }
     * 
     */
    public InsertMessageProcessorType createInsertMessageProcessorType() {
        return new InsertMessageProcessorType();
    }

    /**
     * Create an instance of {@link ExecuteDdlMessageProcessorType }
     * 
     */
    public ExecuteDdlMessageProcessorType createExecuteDdlMessageProcessorType() {
        return new ExecuteDdlMessageProcessorType();
    }

    /**
     * Create an instance of {@link BulkUpdateMessageProcessorType }
     * 
     */
    public BulkUpdateMessageProcessorType createBulkUpdateMessageProcessorType() {
        return new BulkUpdateMessageProcessorType();
    }

    /**
     * Create an instance of {@link ExecuteStoredProcedureMessageProcessorType }
     * 
     */
    public ExecuteStoredProcedureMessageProcessorType createExecuteStoredProcedureMessageProcessorType() {
        return new ExecuteStoredProcedureMessageProcessorType();
    }

    /**
     * Create an instance of {@link AbstractQueryResultSetHandlerType }
     * 
     */
    public AbstractQueryResultSetHandlerType createAbstractQueryResultSetHandlerType() {
        return new AbstractQueryResultSetHandlerType();
    }

    /**
     * Create an instance of {@link TemplateSqlDefinitionType }
     * 
     */
    public TemplateSqlDefinitionType createTemplateSqlDefinitionType() {
        return new TemplateSqlDefinitionType();
    }

    /**
     * Create an instance of {@link ConnectionPropertiesType }
     * 
     */
    public ConnectionPropertiesType createConnectionPropertiesType() {
        return new ConnectionPropertiesType();
    }

    /**
     * Create an instance of {@link CustomDataTypes }
     * 
     */
    public CustomDataTypes createCustomDataTypes() {
        return new CustomDataTypes();
    }

    /**
     * Create an instance of {@link DatabasePoolingProfileType }
     * 
     */
    public DatabasePoolingProfileType createDatabasePoolingProfileType() {
        return new DatabasePoolingProfileType();
    }

    /**
     * Create an instance of {@link AbstractUserAndPasswordDatabaseConfigType }
     * 
     */
    public AbstractUserAndPasswordDatabaseConfigType createAbstractUserAndPasswordDatabaseConfigType() {
        return new AbstractUserAndPasswordDatabaseConfigType();
    }

    /**
     * Create an instance of {@link OracleDatabaseConfigType }
     * 
     */
    public OracleDatabaseConfigType createOracleDatabaseConfigType() {
        return new OracleDatabaseConfigType();
    }

    /**
     * Create an instance of {@link MySqlDatabaseConfigType }
     * 
     */
    public MySqlDatabaseConfigType createMySqlDatabaseConfigType() {
        return new MySqlDatabaseConfigType();
    }

    /**
     * Create an instance of {@link AbstractDbMessageProcessorType }
     * 
     */
    public AbstractDbMessageProcessorType createAbstractDbMessageProcessorType() {
        return new AbstractDbMessageProcessorType();
    }

    /**
     * Create an instance of {@link AbstractDbMixedContentMessageProcessorType }
     * 
     */
    public AbstractDbMixedContentMessageProcessorType createAbstractDbMixedContentMessageProcessorType() {
        return new AbstractDbMixedContentMessageProcessorType();
    }

    /**
     * Create an instance of {@link AdvancedDbMessageProcessorType }
     * 
     */
    public AdvancedDbMessageProcessorType createAdvancedDbMessageProcessorType() {
        return new AdvancedDbMessageProcessorType();
    }

    /**
     * Create an instance of {@link ParameterizedQueryDefinitionType }
     * 
     */
    public ParameterizedQueryDefinitionType createParameterizedQueryDefinitionType() {
        return new ParameterizedQueryDefinitionType();
    }

    /**
     * Create an instance of {@link OutputParamDefinitionType }
     * 
     */
    public OutputParamDefinitionType createOutputParamDefinitionType() {
        return new OutputParamDefinitionType();
    }

    /**
     * Create an instance of {@link InOutParamDefinitionType }
     * 
     */
    public InOutParamDefinitionType createInOutParamDefinitionType() {
        return new InOutParamDefinitionType();
    }

    /**
     * Create an instance of {@link TemplateRefType }
     * 
     */
    public TemplateRefType createTemplateRefType() {
        return new TemplateRefType();
    }

    /**
     * Create an instance of {@link InputParamType }
     * 
     */
    public InputParamType createInputParamType() {
        return new InputParamType();
    }

    /**
     * Create an instance of {@link TemplateInputParamType }
     * 
     */
    public TemplateInputParamType createTemplateInputParamType() {
        return new TemplateInputParamType();
    }

    /**
     * Create an instance of {@link OverriddenTemplateInputParamType }
     * 
     */
    public OverriddenTemplateInputParamType createOverriddenTemplateInputParamType() {
        return new OverriddenTemplateInputParamType();
    }

    /**
     * Create an instance of {@link DatabaseConfigType }
     * 
     */
    public DatabaseConfigType createDatabaseConfigType() {
        return new DatabaseConfigType();
    }

    /**
     * Create an instance of {@link PropertyType }
     * 
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link CustomDataType }
     * 
     */
    public CustomDataType createCustomDataType() {
        return new CustomDataType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SelectMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SelectMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "select", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<SelectMessageProcessorType> createSelect(SelectMessageProcessorType value) {
        return new JAXBElement<SelectMessageProcessorType>(_Select_QNAME, SelectMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "update", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<UpdateMessageProcessorType> createUpdate(UpdateMessageProcessorType value) {
        return new JAXBElement<UpdateMessageProcessorType>(_Update_QNAME, UpdateMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "delete", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<UpdateMessageProcessorType> createDelete(UpdateMessageProcessorType value) {
        return new JAXBElement<UpdateMessageProcessorType>(_Delete_QNAME, UpdateMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InsertMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InsertMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "insert", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<InsertMessageProcessorType> createInsert(InsertMessageProcessorType value) {
        return new JAXBElement<InsertMessageProcessorType>(_Insert_QNAME, InsertMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteDdlMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExecuteDdlMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "execute-ddl", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<ExecuteDdlMessageProcessorType> createExecuteDdl(ExecuteDdlMessageProcessorType value) {
        return new JAXBElement<ExecuteDdlMessageProcessorType>(_ExecuteDdl_QNAME, ExecuteDdlMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BulkUpdateMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BulkUpdateMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "bulk-execute", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-mixed-content-message-processor")
    public JAXBElement<BulkUpdateMessageProcessorType> createBulkExecute(BulkUpdateMessageProcessorType value) {
        return new JAXBElement<BulkUpdateMessageProcessorType>(_BulkExecute_QNAME, BulkUpdateMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteStoredProcedureMessageProcessorType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ExecuteStoredProcedureMessageProcessorType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "stored-procedure", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<ExecuteStoredProcedureMessageProcessorType> createStoredProcedure(ExecuteStoredProcedureMessageProcessorType value) {
        return new JAXBElement<ExecuteStoredProcedureMessageProcessorType>(_StoredProcedure_QNAME, ExecuteStoredProcedureMessageProcessorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractQueryResultSetHandlerType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractQueryResultSetHandlerType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "abstractQueryResultSetHandler")
    public JAXBElement<AbstractQueryResultSetHandlerType> createAbstractQueryResultSetHandler(AbstractQueryResultSetHandlerType value) {
        return new JAXBElement<AbstractQueryResultSetHandlerType>(_AbstractQueryResultSetHandler_QNAME, AbstractQueryResultSetHandlerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TemplateSqlDefinitionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TemplateSqlDefinitionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "template-query", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-extension")
    public JAXBElement<TemplateSqlDefinitionType> createTemplateQuery(TemplateSqlDefinitionType value) {
        return new JAXBElement<TemplateSqlDefinitionType>(_TemplateQuery_QNAME, TemplateSqlDefinitionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "abstract-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-shared-extension")
    public JAXBElement<AbstractExtensionType> createAbstractConfig(AbstractExtensionType value) {
        return new JAXBElement<AbstractExtensionType>(_AbstractConfig_QNAME, AbstractExtensionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectionPropertiesType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConnectionPropertiesType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "connection-properties")
    public JAXBElement<ConnectionPropertiesType> createConnectionProperties(ConnectionPropertiesType value) {
        return new JAXBElement<ConnectionPropertiesType>(_ConnectionProperties_QNAME, ConnectionPropertiesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomDataTypes }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CustomDataTypes }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "data-types")
    public JAXBElement<CustomDataTypes> createDataTypes(CustomDataTypes value) {
        return new JAXBElement<CustomDataTypes>(_DataTypes_QNAME, CustomDataTypes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatabasePoolingProfileType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DatabasePoolingProfileType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "pooling-profile")
    public JAXBElement<DatabasePoolingProfileType> createPoolingProfile(DatabasePoolingProfileType value) {
        return new JAXBElement<DatabasePoolingProfileType>(_PoolingProfile_QNAME, DatabasePoolingProfileType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractUserAndPasswordDatabaseConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractUserAndPasswordDatabaseConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "generic-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/db", substitutionHeadName = "abstract-config")
    public JAXBElement<AbstractUserAndPasswordDatabaseConfigType> createGenericConfig(AbstractUserAndPasswordDatabaseConfigType value) {
        return new JAXBElement<AbstractUserAndPasswordDatabaseConfigType>(_GenericConfig_QNAME, AbstractUserAndPasswordDatabaseConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractUserAndPasswordDatabaseConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AbstractUserAndPasswordDatabaseConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "derby-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/db", substitutionHeadName = "abstract-config")
    public JAXBElement<AbstractUserAndPasswordDatabaseConfigType> createDerbyConfig(AbstractUserAndPasswordDatabaseConfigType value) {
        return new JAXBElement<AbstractUserAndPasswordDatabaseConfigType>(_DerbyConfig_QNAME, AbstractUserAndPasswordDatabaseConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OracleDatabaseConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OracleDatabaseConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "oracle-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/db", substitutionHeadName = "abstract-config")
    public JAXBElement<OracleDatabaseConfigType> createOracleConfig(OracleDatabaseConfigType value) {
        return new JAXBElement<OracleDatabaseConfigType>(_OracleConfig_QNAME, OracleDatabaseConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MySqlDatabaseConfigType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MySqlDatabaseConfigType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "mysql-config", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/db", substitutionHeadName = "abstract-config")
    public JAXBElement<MySqlDatabaseConfigType> createMysqlConfig(MySqlDatabaseConfigType value) {
        return new JAXBElement<MySqlDatabaseConfigType>(_MysqlConfig_QNAME, MySqlDatabaseConfigType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/core", name = "annotations", scope = AbstractDbMixedContentMessageProcessorType.class)
    public JAXBElement<AnnotationsType> createAbstractDbMixedContentMessageProcessorTypeAnnotations(AnnotationsType value) {
        return new JAXBElement<AnnotationsType>(_AbstractDbMixedContentMessageProcessorTypeAnnotations_QNAME, AnnotationsType.class, AbstractDbMixedContentMessageProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParameterizedQueryDefinitionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ParameterizedQueryDefinitionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "parameterized-query", scope = TemplateSqlDefinitionType.class)
    public JAXBElement<ParameterizedQueryDefinitionType> createTemplateSqlDefinitionTypeParameterizedQuery(ParameterizedQueryDefinitionType value) {
        return new JAXBElement<ParameterizedQueryDefinitionType>(_TemplateSqlDefinitionTypeParameterizedQuery_QNAME, ParameterizedQueryDefinitionType.class, TemplateSqlDefinitionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TemplateInputParamType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TemplateInputParamType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "in-param", scope = TemplateSqlDefinitionType.class)
    public JAXBElement<TemplateInputParamType> createTemplateSqlDefinitionTypeInParam(TemplateInputParamType value) {
        return new JAXBElement<TemplateInputParamType>(_TemplateSqlDefinitionTypeInParam_QNAME, TemplateInputParamType.class, TemplateSqlDefinitionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "dynamic-query", scope = TemplateSqlDefinitionType.class)
    public JAXBElement<String> createTemplateSqlDefinitionTypeDynamicQuery(String value) {
        return new JAXBElement<String>(_TemplateSqlDefinitionTypeDynamicQuery_QNAME, String.class, TemplateSqlDefinitionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TemplateRefType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TemplateRefType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "template-query-ref", scope = TemplateSqlDefinitionType.class)
    public JAXBElement<TemplateRefType> createTemplateSqlDefinitionTypeTemplateQueryRef(TemplateRefType value) {
        return new JAXBElement<TemplateRefType>(_TemplateSqlDefinitionTypeTemplateQueryRef_QNAME, TemplateRefType.class, TemplateSqlDefinitionType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TemplateRefType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TemplateRefType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "template-query-ref", scope = ExecuteStoredProcedureMessageProcessorType.class)
    public JAXBElement<TemplateRefType> createExecuteStoredProcedureMessageProcessorTypeTemplateQueryRef(TemplateRefType value) {
        return new JAXBElement<TemplateRefType>(_TemplateSqlDefinitionTypeTemplateQueryRef_QNAME, TemplateRefType.class, ExecuteStoredProcedureMessageProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InputParamType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InputParamType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "in-param", scope = ExecuteStoredProcedureMessageProcessorType.class)
    public JAXBElement<InputParamType> createExecuteStoredProcedureMessageProcessorTypeInParam(InputParamType value) {
        return new JAXBElement<InputParamType>(_TemplateSqlDefinitionTypeInParam_QNAME, InputParamType.class, ExecuteStoredProcedureMessageProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "parameterized-query", scope = ExecuteStoredProcedureMessageProcessorType.class)
    public JAXBElement<String> createExecuteStoredProcedureMessageProcessorTypeParameterizedQuery(String value) {
        return new JAXBElement<String>(_TemplateSqlDefinitionTypeParameterizedQuery_QNAME, String.class, ExecuteStoredProcedureMessageProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutputParamDefinitionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OutputParamDefinitionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "out-param", scope = ExecuteStoredProcedureMessageProcessorType.class)
    public JAXBElement<OutputParamDefinitionType> createExecuteStoredProcedureMessageProcessorTypeOutParam(OutputParamDefinitionType value) {
        return new JAXBElement<OutputParamDefinitionType>(_ExecuteStoredProcedureMessageProcessorTypeOutParam_QNAME, OutputParamDefinitionType.class, ExecuteStoredProcedureMessageProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InOutParamDefinitionType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InOutParamDefinitionType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "inout-param", scope = ExecuteStoredProcedureMessageProcessorType.class)
    public JAXBElement<InOutParamDefinitionType> createExecuteStoredProcedureMessageProcessorTypeInoutParam(InOutParamDefinitionType value) {
        return new JAXBElement<InOutParamDefinitionType>(_ExecuteStoredProcedureMessageProcessorTypeInoutParam_QNAME, InOutParamDefinitionType.class, ExecuteStoredProcedureMessageProcessorType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/db", name = "dynamic-query", scope = ExecuteStoredProcedureMessageProcessorType.class)
    public JAXBElement<String> createExecuteStoredProcedureMessageProcessorTypeDynamicQuery(String value) {
        return new JAXBElement<String>(_TemplateSqlDefinitionTypeDynamicQuery_QNAME, String.class, ExecuteStoredProcedureMessageProcessorType.class, value);
    }

}
