package cn.wizool.bank.iwebutil.newlay;

import java.util.List;

import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.iwebutil.newlay.query.ParameterString;
import cn.wizool.bank.iwebutil.newlay.query.QueryConditions;

public abstract class CommonDataAccessSupport<MODEL> extends DataAccessSupport
		implements ICommonDataAccessSupport<MODEL> {

	private static final String PT_TYPE = "pt_type";

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wizool.fams.common.ICommonDataAccessSupport#create(MODEL)
	 */
	@Override
	public void create(MODEL m) {
		this.getHibernateTemplate().save(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#create(java.util.List)
	 */
	@Override
	public void create(List<MODEL> ms) {
		for (MODEL m : ms) {
			this.create(m);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#delete(java.lang.String)
	 */
	@Override
	public int delete(String id) {
		Object o = this.getHibernateTemplate().load(this.getModelName(), id);
		this.getHibernateTemplate().delete(o);
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#delete(java.lang.String[])
	 */
	@Override
	public int delete(String[] ids) {
		int count = 0;
		for (String id : ids) {
			count += this.delete(id);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#delete(java.lang.String[])
	 */
	@Override
	public int deletes(List<MODEL> models) {
		int count = 0;
		for (MODEL m : models) {
			this.getHibernateTemplate().delete(m);
			count++;
		}
		return count;
	}

	public int delete(MODEL m) {
		int count = 0;
		this.getHibernateTemplate().delete(m);
		count++;
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#delete(java.util.List)
	 */
	@Override
	public int delete(List<String> ids) {
		int count = 0;
		for (String id : ids) {
			count += this.delete(id);
		}
		return count;
	}

	@Override
	public int delete(QueryConditions conditions) {
		if (conditions instanceof NamedConditions) {
			NamedConditions map = (NamedConditions) conditions;
			return this.updateByHql(
					toDelete(this.getHqlTemplate(map.get(PT_TYPE)[0])), map);
		}
		return this.updateByHql(toDelete(toHql(conditions)), null);
	}

	@Override
	public int update(QueryConditions conditions) {
		if (conditions instanceof NamedConditions) {
			NamedConditions map = (NamedConditions) conditions;
			return this.updateByHql(
					toUpdate(this.getHqlTemplate(map.get(PT_TYPE)[0])), map);
		}
		return this.updateByHql(toUpdate(toHql(conditions)), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wizool.fams.common.ICommonDataAccessSupport#update(MODEL)
	 */
	@Override
	public int update(MODEL m) {
		this.getHibernateTemplate().update(m);
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#update(java.util.List)
	 */
	@Override
	public int update(List<MODEL> ms) {
		int count = 0;
		for (MODEL m : ms) {
			count += this.update(m);
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wizool.fams.common.ICommonDataAccessSupport#count()
	 */
	@Override
	public int count() {
		return this.count(toCount(toHql(null)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.wizool.fams.common.ICommonDataAccessSupport#count(cn.wizool.fams.common
	 * .QueryConditions)
	 */
	@Override
	public int count(QueryConditions conditions) {
		if (conditions instanceof NamedConditions) {
			NamedConditions nc = (NamedConditions) conditions;
			return this.countByHql(
					toCount(this.getHqlTemplate(nc.get(PT_TYPE)[0])), nc);
		}
		return this.count(toCount(toHql(conditions)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wizool.fams.common.ICommonDataAccessSupport#select(int, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MODEL> select(int start, int count) {
		return this.query(start, count, toHql(null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.wizool.fams.common.ICommonDataAccessSupport#select(int, int,
	 * cn.wizool.fams.common.QueryConditions)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MODEL> select(int start, int count, QueryConditions conditions) {
		if (conditions instanceof NamedConditions) {
			NamedConditions map = (NamedConditions) conditions;
			return this.query(start, count,
					this.getHqlTemplate(map.get(PT_TYPE)[0]), map);
		}
		return this.query(start, count, toHql(conditions));
	}

	@Override
	public MODEL select(QueryConditions conditions) {
		List<MODEL> list = this.select(0, 1, conditions);
		if (list.size() == 0)
			return null;
		else
			return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public MODEL select(String id) {
		return (MODEL) this.getHibernateTemplate().get(this.getModelName(), id);
	}

	private String toHql(QueryConditions conditions) {
		if (conditions == null) {
			return "from " + this.getModelName();
		}

		return hql(conditions);
	}

	protected String hql(QueryConditions conditions) {
		return "from " + this.getModelName();
	}

	protected ParameterString templates;

	protected ParameterString getTemplate() {
		if (templates == null) {
			templates = new ParameterString();
			this.createHqlTemplates(templates);
		}
		return templates;
	}

	protected void createHqlTemplates(ParameterString tmp) {
		templates.setTemplate("selectAll", "from <model>");
		templates.setParameter("model", this.getModelName());
	}

	protected String getHqlTemplate(String type) {
		return this.getTemplate().getString(type);
	}

	protected abstract String getModelName();

	protected static String toCount(String hql) {
		String hqlStr = hql.replaceFirst(
				"^ *(select +((distinct +)?([^ ]*) +))?from +",
				"select count($4) from ").replace("count()", "count(*)");
		return hqlStr;
	}

	protected static String toUpdate(String hql) {
		return hql.replaceFirst("^ *(select +(distinct)?([^ ]*) )?from +",
				"update $3 from ");
	}

	protected static String toDelete(String hql) {
		return hql.replaceFirst("^ *(select +(distinct)?([^ ]*) )?from +",
				"delete from ");
	}
}
