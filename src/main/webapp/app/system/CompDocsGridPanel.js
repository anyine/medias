Ext.ns('app.view.system');
app.view.system.CompDocsGridPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					layout : 'fit',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/DepartmentServlet?method=getSelectDocsInfo&id="
														+ me.deptId
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'length',
												'uploadDate', 'dept',
												'realitySize', 'updateDate' ]
									})
								});
						me.store = store;
						// me.store.load();
						var sm = new Ext.grid.CheckboxSelectionModel();
						me.sm = sm;
						me.columns = [ sm, new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), {
							header : '文件名称',
							dataIndex : 'name',
							width : 200
						}, {
							header : '大小',
							dataIndex : 'length',
							align : 'right',
							width : 100
						}, {
							header : '实际大小',
							dataIndex : 'realitySize',
							align : 'right',
							width : 100
						}, {
							header : '上传日期',
							dataIndex : 'uploadDate',
							width : 130
						}, {
							header : '修改日期',
							dataIndex : 'updateDate',
							width : 130
						}, {
							header : '上传联社',
							dataIndex : 'dept',
							width : 100
						} ];
						me.tbar = [ '->', '-', {
							xtype : 'button',
							text : '刷新',
							handler : me.query,
							iconCls : 'refresh',
							scope : me
						}, '-', {
							xtype : 'button',
							text : '删除',
							handler : me.del,
							iconCls : 'delete',
							scope : me
						} ]
						app.view.system.CompDocsGridPanel.superclass.initComponent
								.apply(this, arguments);
					},
					query : function() {
						var me = this;
						Ext.Ajax
								.request({
									url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
									params : {
										list : function() {
											var strArray = [];
											strArray
													.push(me.deptId
															+ ';S:f6f2a76c-bf34-4cc7-adba-8d2ab99d0fbb');
											return strArray;
										}()
									},
									success : function(result) {
									}
								});
						me.store.load();
					},
					del : function() {
						var me = this;
						var model = this.getSelectionModel().selections.items;
						var count = model.length;
						if (count != 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认删除选中文件？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
																params : {
																	list : function() {
																		var strArray = [];
																		var str = me.deptId
																				+ ';';
																		for ( var i = 0; i < model.length; i++) {
																			str += 'R:'
																					+ model[i]
																							.get('id')
																					+ ';';
																		}
																		strArray
																				.push(str);
																		return strArray;
																	}()
																},
																success : function(
																		result) {
																}
															});
												}
											})
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					}
				});
