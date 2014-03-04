Ext.ns('app.view.train');
app.view.train.TrainGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					layout : 'fit',
					title : '培训资料',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/TrainServlet?method=get"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'date', 'age',
												'startDate', 'publisherName',
												'dept', 'dateStr' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								name : me.name,
								publisherName : me.publisherName,
								startDate : me.startDate,
								endDate : me.endDate,
								beginDate : me.beginDate,
								overDate : me.overDate
							});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});
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

						var sm = new Ext.grid.CheckboxSelectionModel();
						me.sm = sm;
						me.columns = [ sm, new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), {
							header : '名称',
							dataIndex : 'name',
							width : 200
						}, {
							header : '下发日期',
							dataIndex : 'date',
							width : 140
						}, {
							header : '下发人',
							dataIndex : 'publisherName',
							width : 90
						}, {
							header : '下发机构',
							dataIndex : 'dept',
							width : 140
						}, {
							header : '时长',
							dataIndex : 'age',
							align : 'right'
						}, {
							header : '开始日期',
							dataIndex : 'startDate',
							width : 150
						} ];
						var name = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.name = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.name = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var startDate = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.startDate = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.startDate = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var endDate = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.endDate = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.endDate = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var publisherName = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.publisherName = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.publisherName = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var beginDate = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.beginDate = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.beginDate = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var overDate = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.overDate = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.overDate = this.getValue();
										me.query(me);
									}
								}
							}
						});
						me.tbar = [ {
							xtype : 'label',
							text : '名称：'
						}, name, {
							xtype : 'label',
							text : '下发日期 从：'
						}, startDate, {
							xtype : 'label',
							text : '到：'
						}, endDate, {
							xtype : 'label',
							text : '下发单位：'
						}, publisherName, {
							xtype : 'label',
							text : '开始日期 从：'
						}, beginDate, {
							xtype : 'label',
							text : '到：'
						}, overDate, '-', {
							xtype : 'button',
							text : '查询',
							iconCls : 'query',
							handler : me.query,
							scope : me
						}, '->', '-', {
							xtype : 'button',
							text : '详细信息',
							iconCls : 'selectinfo',
							handler : me.selectInfo,
							scope : me
						}, '-', {
							text : '下发包含文件',
							iconCls : 'sendDocs',
							handler : me.sendDocs,
							scope : me
						}, '-', {
							xtype : 'button',
							text : '新增',
							iconCls : 'create',
							handler : function(button) {
								var win = new app.view.train.TrainEditWindow({
									title : '新增培训资料',
									store : me.store
								});
								win.show();
							}
						}, {
							xtype : 'button',
							text : '修改',
							iconCls : 'modify',
							handler : me.modify,
							scope : me
						}, {
							xtype : 'button',
							text : '删除',
							iconCls : 'delete',
							handler : me.del,
							scope : me
						} ];
						me.on('dblclick', me.modify);
						app.view.train.TrainGridPanel.superclass.initComponent
								.apply(this, arguments);
					},
					sendDocs : function() {
						var me = this;
						var node = me.getSelectionModel().selections.items;
						if (node.length == 1) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认要给该任务包含的机器下发所包含的文件？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/TrainServlet?method=sendDocs',
																params : {
																	taskId : node[0]
																			.get('id')
																},
																success : function(
																		response) {
																}
															});
												}
											}, this);
						} else {
							Ext.Msg.alert('提示', '请选择一项');
						}
					},
					selectInfo : function() {
						var me = this;
						var node = this.getSelectionModel().selections.items;
						if (node.length == 1) {
							var win = new app.view.media.SelectInfoWindow({
								width : 600,
								height : 480,
								iconCls : 'selectinfo',
								taskId : node[0].get("id")
							});
							win.show();
						} else {
							Ext.Msg.alert('提示', '请选择一项');
						}
					},
					modify : function() {
						var me = this;
						var node = this.getSelectionModel().selections.items;
						if (node.length == 1) {
							Ext.Ajax
									.request({
										url : 'cn/wizool/bank/servlet/TrainServlet?method=clickModify',
										params : {
											taskId : node[0].get('id'),
											type : 'train'
										},
										success : function(response) {
											var text = Ext
													.decode(response.responseText);
											var win = new app.view.train.TrainEditWindow(
													{
														title : '修改培训资料',
														oldId : text.oldId,
														store : me.store,
														oldDocIds : text.docIds,
														oldDeptIds : text.deptIds
													});
											var typeSt = new Ext.data.SimpleStore(
													{
														fields : [ 'id', 'name' ],
														data : [ [ '开', '开' ],
																[ '关', '关' ] ]
													});
											win.show();

											win.findByType('textfield')[0]
													.setValue(text.name);
											win.findByType('textfield')[1]
													.setValue(text.startDate);
											win.findByType('combo')[0]
													.setValue(text.hour);
											win.findByType('combo')[1]
													.setValue(text.minute);
										}
									});
						} else {
							Ext.Msg.alert('提示', '请选择一项');
						}
					},
					del : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length > 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认要删除任务？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/TrainServlet?method=delete',
																params : {
																	ids : function() {
																		var strArray = [];
																		for ( var i = 0; i < model.length; i++) {
																			strArray
																					.push(model[i]
																							.get('id'));
																		}
																		return strArray;
																	}()
																},
																success : function(
																		response) {
																	var text = Ext
																			.decode(response.responseText);
																	var flag = text.success;
																	if (flag) {
																		Ext.example
																				.msg(
																						"提示",
																						"操作成功");
																		me.store
																				.load({
																					params : {
																						start : 0,
																						limit : 25
																					}
																				});
																	} else {
																		Ext.Msg
																				.alert(
																						"提 示",
																						text.message);
																	}
																}
															});
												}
											}, this);
						} else {
							Ext.Msg.alert('提示', '请选择一项');
						}
					},
					query : function() {
						var me = this;
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});
					}
				});
