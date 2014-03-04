Ext.ns('app.view.dept');
app.view.dept.MachinePanel = Ext
		.extend(
				Ext.Panel,
				{
					layout : 'border',
					title : '机器列表',
					style : 'height:100%;width:100%;padding: 0px 5px 0px 6px; background-color:#dfe8f6',
					initComponent : function() {
						var me = this;
						var treepanel = new app.view.dept.DeptTreePanel({
							title : '',
							dbcheck : true,
							region : 'west',
							autoScroll : true,
							containerScroll : true,
							classify : 'machine',
							width : '19%'
						});
						treepanel.on('click', function(node) {
							me.branchId = node.id;
							me.query(me);
						});
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : 'cn/wizool/bank/servlet/DepartmentServlet?method=getMachines'
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'ip', 'type',
												'parentName', 'linkman',
												'phone', 'mobilephone' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								branchId : me.branchId,
								type : me.type
							});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 30
							}
						});
						var sm = new Ext.grid.CheckboxSelectionModel();
						me.sm = sm;
						var typeStore = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '广告', '广告' ], [ '培训', '培训' ],
									[ '全部', '全部' ] ]
						});
						var type = new Ext.form.ComboBox({
							xtype : 'combo',
							store : typeStore,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 60,
							editable : false,
							listeners : {
								select : function() {
									var type = this;
									me.type = type.getRawValue();
									me.query(me);
								},
								blur : function() {
									var type = this;
									me.type = type.getRawValue();
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.query(me);
									}
								}
							}
						});
						var gridpanel = new Ext.grid.GridPanel(
								{
									region : 'center',
									store : me.store,
									height : '100%',
									columns : [ sm, new Ext.grid.RowNumberer({
										width : 40,
										header : '序号'
									}), {
										header : '机器所属',
										dataIndex : 'parentName',
										width : 140
									}, {
										header : '机器名称',
										dataIndex : 'name',
										width : 90
									}, {
										header : '机器IP',
										dataIndex : 'ip',
										width : 110
									}, {
										header : '机器类型',
										dataIndex : 'type',
										width : 80
									}, {
										header : '联系人',
										dataIndex : 'linkman',
										width : 80
									}, {
										header : '座机号',
										dataIndex : 'phone',
										width : 140
									}, {
										header : '手机',
										dataIndex : 'mobilephone',
										width : 140
									} ],
									tbar : [
											{
												xtype : 'label',
												text : '类型:'
											},
											type,
											'-',
											{
												xtype : 'button',
												text : '查询',
												iconCls : 'query',
												handler : me.query,
												scope : me
											},
											'->',
											{
												xtype : 'button',
												text : '新增',
												iconCls : 'create',
												handler : function(button) {
													var win = new app.view.dept.MachineEditWindow(
															{
																title : '新增',
																iconCls : 'create',
																store : me.store
															});
													win.show();
												},
											}, {
												xtype : 'button',
												text : '修改',
												iconCls : 'modify',
												handler : me.clickModify,
												scope : me
											}, {
												xtype : 'button',
												text : '删除',
												iconCls : 'delete',
												handler : me.del,
												scope : me
											} ],
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
						me.items = [ treepanel, gridpanel ];
						gridpanel.on('dblclick', me.dbclick);
						app.view.dept.MachinePanel.superclass.initComponent
								.apply(this, arguments);
					},
					query : function() {
						var me = this;
						me.store.reload();
					},
					dbclick : function() {
						var me = this;
						var win = me.findParentByType();
						win.modify(me);
					},
					clickModify : function() {
						var me = this;
						var grid = me.findByType('grid')[0];
						me.modify(grid);
					},
					modify : function(grid) {
						var node = grid.getSelectionModel().getSelected().data;
						if (node == null) {
							Ext.Msg.alert('提示', '请选择一项');
						} else {
							var win = new app.view.dept.MachineEditWindow({
								title : '修改',
								iconCls : 'modify',
								modelId : node.id,
								store : grid.view.grid.store
							});
							win.show();
							win.findByType('combo')[0].setValue(node.type);
							win.findByType('textfield')[0]
									.setValue(node.parentName);
							win.findByType('textfield')[1].setValue(node.name);
							win.findByType('textfield')[2].setValue(node.ip);
							win.findByType('textfield')[3]
									.setValue(node.linkman);
							win.findByType('textfield')[4].setValue(node.phone);
							win.findByType('textfield')[5]
									.setValue(node.mobilephone);
						}
					},
					del : function() {
						var me = this;
						var grid = me.findByType('grid')[0];
						var model = grid.getSelectionModel().selections.items;
						if (model.length > 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认要删除机器？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/DepartmentServlet?method=deleteMachine',
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
																		grid.view.grid.store
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
																						'提示',
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
