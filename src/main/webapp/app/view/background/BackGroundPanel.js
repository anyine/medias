Ext.ns('app.view.background');
app.view.background.BackGroundPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					layout : 'fit',
					title : '背景图片管理',
					height : 400,
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/DocumentServlet?method=getBackGroundFile"
											}),
									reader : new Ext.data.JsonReader({
										root : 'root',
										totalProperty : 'total',
										fields : [ 'id', 'uploaddate',
												'length', 'name', 'type',
												'dept', 'user' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								type : me.type,
								fileName : me.fileName
							});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});
						var sm = new Ext.grid.CheckboxSelectionModel();
						me.sm = sm;
						me.columns = [ sm, new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), {
							header : '名称',
							dataIndex : 'name',
							width : 400
						}, {
							header : '大小',
							dataIndex : 'length',
							align : 'right',
							width : 70
						}, {
							header : '文件类型',
							dataIndex : 'type',
							width : 90
						}, {
							header : '上传人',
							dataIndex : 'user',
							width : 90
						}, {
							header : '上传联社',
							dataIndex : 'dept',
							width : 140
						}, {
							header : '上传日期',
							dataIndex : 'uploaddate',
							width : 130
						} ];

						var types = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '视频', '视频' ], [ '图片', '图片' ],
									[ '幻灯片', '幻灯片' ], [ 'Word文档', 'Word文档' ],
									[ 'Excel表格', 'Excel表格' ],
									[ 'PDF文档', 'PDF文档' ], [ 'GD文档', 'GD文档' ],
									[ '其他', '其他' ], [ '全部', '全部' ] ]
						});
						var cb1 = new Ext.form.ComboBox({
							xtype : 'combo',
							store : types,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 100,
							editable : false,
							listeners : {
								select : function() {
									var cb1 = this;
									me.type = cb1.getRawValue();
									me.query(me);
								},
								blur : function() {
									var cb1 = this;
									me.type = cb1.getRawValue();
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.query(me);
									}
								}
							}
						});
						var name = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.fileName = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.fileName = this.getValue();
										me.query(me);
									}
								}
							}
						});
						me.tbar = [
								{
									xtype : 'label',
									text : '文件类型:'
								},
								cb1,
								'-',
								{
									xtype : 'label',
									text : '文件名称:'
								},
								name,
								'-',
								{
									xtype : 'button',
									text : '查询',
									iconCls : 'query',
									handler : function() {
										me.query(me);
									}
								},
								'->',
								{
									xtype : 'button',
									text : '本地上传',
									hidden : me.buttonIsHidden,
									iconCls : 'localupload',
									hidden : me.buttonIsHidden,
									handler : function(button) {
										var win = new app.view.background.FileShareEditWindow(
												{
													store : me.store,
													iconCls : 'localupload',
													title : '本地上传'
												});
										win.show();
									}
								}, {
									xtype : 'button',
									hidden : me.buttonIsHidden,
									text : '下载图片',
									iconCls : 'download',
									handler : me.download,
									scope : me
								}, {
									xtype : 'button',
									hidden : me.buttonIsHidden,
									text : '删除',
									iconCls : 'delete',
									handler : me.del,
									scope : me
								}, {
									xtype : 'button',
									hidden : me.buttonIsHidden,
									text : '选中作为背景',
									iconCls : 'check',
									handler : me.checkForBack,
									scope : me
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
						app.view.background.BackGroundPanel.superclass.initComponent
								.apply(this, arguments);
					},
					query : function() {
						var me = this;
						me.store.reload({
							params : {
								start : 0,
								limit : 25
							}
						});
					},
					del : function() {
						var me = this;
						var model = this.getSelectionModel().selections.items;
						var count = model.length;
						if (count != 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'您将删除此文件？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/DocumentServlet?method=deleteBack',
																params : {
																	ids : function() {
																		var strArray = [];
																		for ( var i = 0; i < count; i++) {
																			strArray
																					.push(model[i].data.id);
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
							Ext.Msg.alert("提 示", "请选择一项");
						}
					},
					send : function() {
						var me = this;
						var model = this.getSelectionModel().selections.items;
						var count = model.length;
						if (count != 0) {
							var win = new app.view.dept.DeptCheckWindow({
								iconCls : 'tasksend'
							});
							win
									.on(
											'beforeclose',
											function() {
												var id = this.ids.split(" ");
												for ( var i = 0; i < id.length; i++) {
													if (id[i] != "") {
														for ( var j = 0; j < model.length; j++) {
															Ext.Ajax
																	.request({
																		url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
																		params : {
																			list : function() {
																				var strArray = [];
																				strArray
																						.push(id[i]
																								+ ';D:'
																								+ model[j]
																										.get('id'));
																				return strArray;
																			}()
																		},
																		success : function(
																				result) {

																		}
																	});
														}
													}
												}
											});
							win.show();
						} else {
							Ext.Msg.alert("提 示", "请选择一项");
						}
					},
					checkForBack : function() {
						var me = this;
						var rows = this.getSelectionModel().getSelections().length;
						if (rows == 1) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'你确定将此图片作为广告机背景图片？',
											function(btn) {
												if (btn == 'yes') {
													var r = this
															.getSelectionModel()
															.getSelected();
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/DocumentServlet?method=chooseForBack',
																params : {
																	id : r.data['id']
																},
																success : function(
																		result) {
																	me.store
																			.reload();
																	Ext.example
																			.msg(
																					"提示",
																					"操作成功");

																}
															});
												}
											}, this);

						} else if (rows > 1) {
							Ext.Msg.alert("提 示", "只能选择一项作为背景图片");
						} else {
							Ext.Msg.alert("提 示", "请选择一项");
						}
					},
					download : function() {
						var me = this;
						var model = this.getSelectionModel().selections.items;
						if (model.length == 1) {
							Ext.Msg
									.confirm(
											"提示",
											"下载此图片？",
											function(btn) {
												if (btn == 'yes') {
													var obj = model[0].data;
													document.location.href = 'cn/wizool/bank/servlet/DocumentServlet?method=download&id='
															+ obj.id;
												}
											}, this);
						} else {
							Ext.Msg.alert("提 示", "请选择一项");
						}
					}
				});