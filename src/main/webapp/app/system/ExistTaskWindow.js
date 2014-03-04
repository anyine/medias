Ext.ns('app.system');
app.system.ExistTaskWindow = Ext
		.extend(
				Ext.Window,
				{
					layout : 'fit',
					title : '任务列表',
					buttonAlign : 'center',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/MediaServlet?method=get"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'dispatch',
												'age', 'enabled', 'date',
												'publisherName' ]
									})
								});
						me.store = store;
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});

						me.items = [ {
							xtype : 'grid',
							store : me.store,
							columns : [ new Ext.grid.RowNumberer({
								width : 40,
								header : '序号'
							}), {
								header : '名称',
								dataIndex : 'name',
								width : 120
							}, {
								header : '下发日期',
								dataIndex : 'date',
								width : 120
							}, {
								header : '下发单位',
								dataIndex : 'publisherName',
								width : 110
							}, {
								header : '调度方式',
								dataIndex : 'dispatch',
								width : 120
							}, {
								header : '时长(秒)',
								dataIndex : 'age',
								align : 'right'
							}, {
								header : '调度开关',
								dataIndex : 'enabled'
							} ],
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
							}),
							listeners : {
								dblclick : me.dblclick
							}
						} ];
						me.buttons = [ {
							xtype : 'button',
							text : '保存',
							handler : me.save,
							scope : me
						}, {
							xtype : 'button',
							text : '取消',
							handler : function(button) {
								me.close();
							}
						} ];

						app.system.ExistTaskWindow.superclass.initComponent
								.apply(this, arguments);
					},
					save : function() {
						var me = this;
						var depts = me.depts;
						var model = me.findByType('grid')[0]
								.getSelectionModel().selections.items;
						if (model.length == 1) {
							var taskId = model[0].get('id');
							me.ajax(taskId, depts, me);
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					dblclick : function() {
						var me = this;
						var win = me.findParentByType();
						var model = me.getSelectionModel().selections.items;
						var depts = win.depts;
						if (model.length == 1) {
							var taskId = model[0].get('id');
							win.ajax(taskId, depts, win);
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					ajax : function(taskId, depts, win) {
						Ext.Msg
								.confirm(
										'提示窗口？',
										'确定给选择机器播放该任务？',
										function(btn) {
											if (btn == 'yes') {
												Ext.Ajax
														.request({
															url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
															params : {
																list : function() {
																	var strArray = [];
																	for ( var i = 0; i < depts.length; i++) {
																		strArray
																				.push(depts[i]
																						+ ';S:'
																						+ taskId);
																	}
																	return strArray;
																}()
															},
															success : function(
																	result) {
															}
														});
												win.close();
											}
										})
					}
				});
