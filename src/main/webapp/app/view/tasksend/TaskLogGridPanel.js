Ext.ns('app.view.tasksend');
app.view.tasksend.TaskLogGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					taskId : '',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/TaskLogServlet?method=getByTaskId"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'taskName', 'type',
												'startTime', 'endTime',
												'taskId', 'deptId', 'downTime',
												'locationName', 'pcName',
												'DepName' ]
									})
								});

						me.store = store;

						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								taskId : me.taskId,
								taskSendId : me.taskSendId
							});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 150
							}
						});
						me.columns = [ new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), {
							header : '联社名称',
							width : 160,
							dataIndex : 'DepName'
						}, {
							header : '网点名称',
							width : 170,
							dataIndex : 'locationName'
						}, {
							header : '计算机名称',
							width : 100,
							dataIndex : 'pcName'
						}, {
							header : '任务名称',
							width : 110,
							dataIndex : 'taskName'
						}
						// , {
						// header : '任务类型',
						// width : 100,
						// dataIndex : 'type'
						// }
						, {
							header : '下载时间',
							width : 160,
							dataIndex : 'downTime'
						}, {
							header : '打开时间',
							width : 160,
							dataIndex : 'startTime'
						}, {
							header : '关闭时间',
							width : 160,
							dataIndex : 'endTime'
						} ];
						Ext.applyIf(me, {
							bbar : new Ext.PagingToolbar({
								store : me.store,
								pageSize : 150,
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

						me.tbar = [ '->', '-', {
							xtype : 'button',
							text : '全部结束',
							handler : me.killAll,
							iconCls : 'delete',
							scope : me
						} ]
						app.view.tasksend.TaskLogGridPanel.superclass.initComponent
								.apply(this, arguments);
					},
					killAll : function() {
						var me = this;
						me.store
								.each(
										function(a, b) {
											Ext.Ajax
													.request({
														url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
														params : {
															list : function() {
																var strArray = [];
																strArray
																		.push(a
																				.get('deptId')
																				+ ';K:'
																				+ a
																						.get('taskId'));
																return strArray;
															}()
														},
														success : function(
																result) {
															me.store.load({
																params : {
																	start : 0,
																	limit : 150
																}
															});
														}
													});
										}, me);
					}
				})