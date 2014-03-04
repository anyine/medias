package cn.wizool.bank.iwebutil.newlay;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;

public abstract class DataAccessSupport extends HibernateDaoSupport {

	private DataAccessFactory dataAccessFactory;

	/**
	 * 根据HQL语句，返回从start开始的，count条查询结果。
	 * 
	 * @param start
	 *            返回结果的开始位置，从零开始，即：第一条数据为0。
	 * @param count
	 *            最多返回结果的数量，当数据不足时返回记录数可以小于该数量
	 * @param hql
	 *            HQL查询语句
	 * @return 返回查询结果的列表，当查询结果为空时，返回一个数量为0的列表对象，而不是NULL。
	 */
	@SuppressWarnings("rawtypes")
	public List query(final int start, final int count, final String hql) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<List>() {
					@Override
					public List doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						query.setFirstResult((int) start);
						query.setMaxResults((int) count);
						List list = query.list();
						return list;
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public int updateByHql(final String hql, final NamedConditions conditions) {
		final Integer[] count = new Integer[1];
		this.getHibernateTemplate().execute(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (conditions != null) {
					String params[] = findParameters(hql);
					setParameters(query, conditions, params);
				}
				count[0] = query.executeUpdate();
				return null;
			}
		});
		return count[0];
	}

	@SuppressWarnings("rawtypes")
	public int countByHql(final String hql, final NamedConditions conditions) {
		List list = this.getHibernateTemplate().execute(
				new HibernateCallback<List>() {
					@Override
					public List doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						if (conditions != null) {
							String params[] = findParameters(hql);
							setParameters(query, conditions, params);
						}
						return query.list();
					}
				});
		return ((Long) list.get(0)).intValue();
	}

	/**
	 * 根据HQL语句，返回从start开始的，count条查询结果。
	 * 
	 * @param start
	 *            返回结果的开始位置，从零开始，即：第一条数据为0。
	 * @param count
	 *            最多返回结果的数量，当数据不足时返回记录数可以小于该数量
	 * @param hql
	 *            HQL查询语句
	 * @return 返回查询结果的列表，当查询结果为空时，返回一个数量为0的列表对象，而不是NULL。
	 */
	@SuppressWarnings("rawtypes")
	public List query(final int start, final int count, final String hql,
			final NamedConditions conditions) {
		return this.getHibernateTemplate().execute(
				new HibernateCallback<List>() {
					@Override
					public List doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						String params[] = findParameters(hql);
						query.setFirstResult((int) start);
						query.setMaxResults((int) count);
						setParameters(query, conditions, params);
						List list = query.list();
						return list;
					}
				});
	}

	private String[] findParameters(String hql) {
		String find = ":e?[idtsb]a?[A-Z][A-Za-z0-9]*";
		Set<String> strs = new HashSet<String>();
		Pattern p = Pattern.compile(find);
		Matcher matcher = p.matcher(hql);
		while (matcher.find()) {
			strs.add(matcher.group().substring(1));
		}
		return strs.toArray(new String[0]);
	}

	@SuppressWarnings("unchecked")
	protected void setParameters(Query query, NamedConditions conditions,
			String[] params) {
		for (String param : params) {
			if (param.startsWith("e")) {
				// 是否存在参数
				query.setBoolean(param, conditions.exists(param.substring(1)));
			} else if (param.charAt(1) == 'a') {
				// 设置数组参数
				query.setParameterList(param,
						(Collection<Object>) conditions.toObject(param));
			} else {
				// 设置非数组参数
				query.setParameter(param, conditions.toObject(param));
			}
		}
	}

	/**
	 * 返回查询结果的总数量（数据库中满足查询语句的记录总数）
	 * 
	 * @param hql
	 *            HQL查询语句（不能包括“select count(*) ”）
	 * @return 结果数量
	 */
	public int count(String hql) {
		return ((Long) getHibernateTemplate().find(hql).listIterator().next())
				.intValue();
	}

	public void setDataAccessFactory(DataAccessFactory dataAccessFactory) {
		this.dataAccessFactory = dataAccessFactory;
	}

	public DataAccessFactory getDataAccessFactory() {
		return dataAccessFactory;
	}
}
