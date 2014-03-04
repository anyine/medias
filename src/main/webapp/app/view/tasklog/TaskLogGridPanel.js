Ext.ns('app.view.tasklog');
app.view.tasklog.TaskLogGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					checked : false,
					kill : true,
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/TaskLogServlet?method=getNoticeLogs"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'taskName', 'birth',
												'taskId', 'person' ]
									})
								});
						if (me.checked) {
							me
									.on(
											'dblclick',
											function() {
												var node = me
														.getSelectionModel().selections.items;
												window.location.href = 'app://'
														+ node[0].get('taskId');
											});
						}
						me.store = store;
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});
						me.columns = [ new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), {
							header : '任务名称',
							width : 200,
							dataIndex : 'taskName'
						}, {
							header : '创建机构',
							width : 200,
							dataIndex : 'person'
						}, {
							header : '创建时间',
							width : 200,
							dataIndex : 'birth'
						} ];

						Ext.applyIf(me, {
							bbar : new Ext.PagingToolbar({
								store : me.store,
								pageSize : 25,
								displayInfo : false,
								emptyMsg : "",
								prevText : "上一页",
								nextText : "下一页",
								refreshText : "刷新",
								lastText : "最后页",
								firstText : "第一页",
								beforePageText : "当前页",
								displayInfo : true,
								displayMsg : '第{0} 到 {1} 条，共 {2}条',
								afterPageText : "共{0}页"
							})
						});
						app.view.tasklog.TaskLogGridPanel.superclass.initComponent
								.apply(this, arguments);
					}
				});