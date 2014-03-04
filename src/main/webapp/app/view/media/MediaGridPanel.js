Ext.ns('app.view.media');
app.view.media.MediaGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					layout : 'fit',
					title : '广告媒体',
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
												'publisherName', 'dept',
												'startDate', 'endDate', 'hour',
												'minute' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								name : me.name,
								enabled : me.enabled,
								publisherName : me.publisherName,
								startDate : me.startDate,
								endDate : me.endDate
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
							header : '调度方式',
							dataIndex : 'dispatch',
							width : 140
						}, {
							header : '时长',
							dataIndex : 'age',
							align : 'right'
						}, {
							header : '调度开关',
							dataIndex : 'enabled'
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
						var st = new Ext.data.SimpleStore(
								{
									fields : [ 'id', 'name' ],
									data : [ [ '开', '开' ], [ '关', '关' ],
											[ '全部', '全部' ] ]
								});
						var com = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 50,
							editable : false,
							listeners : {
								select : function() {
									var com = this;
									var res = com.getRawValue();
									if (res == "开") {
										me.enabled = '1';
									} else if (res == "关") {
										me.enabled = "0"
									} else if (res == "全部") {
										me.enabled = "2"
									} else {
										me.enabled = "";
									}
									me.query(me);
								},
								blur : function() {
									var com = this;
									var res = com.getRawValue();
									if (res == "开") {
										me.enabled = '1';
									} else if (res == "关") {
										me.enabled = "0"
									} else {
										me.enabled = "";
									}
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										var com = this;
										var res = com.getRawValue();
										if (res == "开") {
											me.enabled = '1';
										} else if (res == "关") {
											me.enabled = "0"
										} else {
											me.enabled = "";
										}
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
							text : '调度开关：'
						}, com, '-', {
							text : '查询',
							iconCls : 'query',
							handler : me.query,
							scope : me
						}, '->', '-', {
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
							text : '新增',
							iconCls : 'create',
							handler : function(button) {
								var win = new app.view.media.MediaEditWindow({
									title : '新增广告媒体',
									store : me.store
								});
								win.show();
							}
						}, {
							text : '修改',
							iconCls : 'modify',
							handler : me.modify,
							scope : me
						}, {
							text : '删除',
							iconCls : 'delete',
							handler : me.del,
							scope : me
						} ];
						me.on('dblclick', me.modify);
						app.view.media.MediaGridPanel.superclass.initComponent
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
																url : 'cn/wizool/bank/servlet/MediaServlet?method=sendDocs',
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
										url : 'cn/wizool/bank/servlet/MediaServlet?method=clickModify',
										params : {
											taskId : node[0].get('id'),
											type : 'media'
										},
										success : function(response) {
											var text = Ext
													.decode(response.responseText);
											var win = new app.view.media.MediaEditWindow(
													{
														title : '修改广告媒体',
														oldId : text.oldId,
														store : me.store,
														oldDeptIds : text.deptIds
													});
											var typeSt = new Ext.data.SimpleStore(
													{
														fields : [ 'id', 'name' ],
														data : [ [ '开', '开' ],
																[ '关', '关' ] ]
													});
											var form = win.findByType('form')[0].items.items[0].items.items[1];
											form
													.insert(
															5,
															{
																xtype : 'combo',
																fieldLabel : '调度开关',
																store : typeSt,
																editable : false,
																triggerAction : 'all',
																valueField : 'name',
																mode : 'local',
																displayField : 'name',
																name : 'enabled',
																anchor : '99%',
																width : 80,
																listeners : {
																	select : function(
																			a,
																			b) {
																		var s = b
																				.get('name');
																		if (s == "开") {
																			win
																					.findByType('hidden')[0]
																					.setValue('true');
																		} else if (s == "关") {
																			win
																					.findByType('hidden')[0]
																					.setValue('false');
																		}
																	}
																}
															});
											win.show();
											win.findByType('textfield')[0]
													.setValue(text.name);
											win.findByType('combo')[0]
													.setValue(text.h);
											win.findByType('combo')[1]
													.setValue(text.m);
											win.findByType('combo')[2]
													.setValue(text.enabled);
											if (text.dispatch == '') {
												Ext.getCmp("wudiaodu")
														.setValue(true);

											} else if (text.endDate != '') {
												Ext.getCmp("date01").setValue(
														text.startDate);
												Ext.getCmp("date02").setValue(
														text.endDate);
												Ext.getCmp("sjd")
														.setValue(true);
											} else {
												Ext.getCmp("date01").setValue(
														text.startDate);
												Ext.getCmp("myt")
														.setValue(true);
											}

											win.findByType('numberfield')[0]
													.setValue(text.hour);
											win.findByType('numberfield')[1]
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
																url : 'cn/wizool/bank/servlet/MediaServlet?method=delete',
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
