Ext.ns('app.view.dept');
app.view.dept.UserGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					layout : 'fit',
					title : '机器列表',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/DepartmentServlet?method=getUsers"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'parentName' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 30
							}
						});
						Ext.applyIf(me, {
							bbar : new Ext.PagingToolbar({
								store : me.store,
								pageSize : 30,
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
							header : '用户名称',
							dataIndex : 'name',
							width : 80
						}, {
							header : '用户所属',
							dataIndex : 'parentName',
							width : 140
						} ];
						me.tbar = [ '->', {
							xtype : 'button',
							text : '新增',
							iconCls : 'create',
							handler : function(button) {
								var win = new app.view.dept.UserEditWindow({
									title : '新增',
									iconCls : 'create',
									store : me.store,
									method : 'createUser'
								});
								win.show();
							}
						}, {
							xtype : 'button',
							text : '删除',
							iconCls : 'delete',
							handler : me.del,
							scope : me
						} ];
						app.view.dept.UserGridPanel.superclass.initComponent
								.apply(this, arguments);
					},
					del : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length > 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认要删除用户？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/DepartmentServlet?method=deleteUser',
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
																		me.store
																				.load({
																					params : {
																						start : 0,
																						limit : 30
																					}
																				});
																		Ext.example
																				.msg(
																						"提示",
																						"操作成功");
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
					}
				});
