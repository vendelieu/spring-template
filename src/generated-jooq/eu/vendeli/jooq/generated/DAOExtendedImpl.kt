package eu.vendeli.jooq.impl

import org.jooq.*
import org.jooq.impl.DAOImpl

abstract class DAOExtendedImpl<Record : UpdatableRecord<Record>, Pojo, Id> protected constructor(
    table: Table<Record>,
    type: Class<Pojo>
) : DAOImpl<Record, Pojo, Id>(table, type) {
    protected constructor(table: Table<Record>, type: Class<Pojo>, configuration: Configuration) : this(table, type) {
        super.setConfiguration(configuration)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected val dslContext: DSLContext get() = super.ctx()

    private fun <T : Record> mapRecord(block: () -> Record?): Pojo? = mapper().map(block())

    fun getOne(block: () -> Condition): Pojo? = mapRecord { dslContext.fetchOne(table, block()) }
    fun getSingle(block: () -> Condition): Pojo = mapRecord { dslContext.fetchSingle(table, block()) }!!
    fun getAll(block: () -> Condition): List<Pojo> = dslContext.fetch(table, block()).map { mapRecord { it } }

    fun create(block: Pojo.() -> Unit): Pojo {
        val pojo = mapRecord { dslContext.newRecord(table) }!!.apply(block)
        insert(pojo)

        return pojo
    }

    fun update(id: Id, block: Pojo.() -> Unit): Pojo? {
        val pojo: Pojo? = findById(id)?.apply(block) ?: return null
        update(pojo)

        return pojo
    }

    fun update(condition: Condition, block: Pojo.() -> Unit): Pojo? {
        val pojo = mapRecord { dslContext.fetchOne(table, condition) }?.apply(block) ?: return null
        update(pojo)

        return pojo
    }

    fun delete(block: () -> Condition) = dslContext.deleteFrom(table).where(block()).execute()

    fun count(block: () -> Condition): Int = dslContext.fetchCount(table, block())
}
