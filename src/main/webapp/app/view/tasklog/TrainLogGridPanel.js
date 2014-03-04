Ext.ns('app.view.tasklog');
app.view.tasklog.TrainLogGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					title : '培训资料历史',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/TaskLogServlet?method=getTrainLogs"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'start',
												'deptName' ]
									})
								});

						me.tbar = [];

						me
								.on(
										'dblclick',
										function() {
											var node = me.getSelectionModel().selections.items;
											window.location.href = 'app://'
													+ node[0].get('id');
										});
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
							dataIndex : 'name'
						}, {
							header : '开始时间',
							width : 200,
							dataIndex : 'start'
						}, {
							header : '下发机构',
							width : 200,
							dataIndex : 'deptName'
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
						app.view.tasklog.TrainLogGridPanel.superclass.initComponent
								.apply(this, arguments);
					}
				});