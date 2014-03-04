Ext.ns('app.view.media');
app.view.media.LocalUploadWindow = Ext
		.extend(
				Ext.Window,
				{
					width : 600,
					buttonAlign : 'center',
					modal : true,
					datas : [],
					initComponent : function() {
						var me = this;

						var fileStore = new Ext.data.SimpleStore({
							fields : [ 'name', 'size', 'type',
									'lastModifiedDate' ],
							data : []
						});
						var panel = new Ext.grid.GridPanel({
							layout : 'fit',
							height : 300,
							store : fileStore,
							columns : [ new Ext.grid.RowNumberer({
								width : 40,
								header : '序号'
							}), {
								header : '名称',
								dataIndex : 'name',
								width : 188
							}, {
								header : '大小(字节)',
								dataIndex : 'size',
								width : 90,
								align : 'right'
							}, {
								header : '类型',
								dataIndex : 'type',
								width : 80
							}, {
								header : '修改时间',
								dataIndex : 'lastModifiedDate',
								width : 120
							} ],
							tbar : [ '->', {
								xtype : 'button',
								iconCls : 'upFirst',
								tooltip : '置顶',
								text : '置顶',
								handler : me.upFirst,// 置顶
								scope : me
							}, {
								xtype : 'button',
								iconCls : 'upOne',
								tooltip : '上移',
								text : '上移',
								handler : me.upOne,// 向上移动一个
								scope : me
							}, {
								xtype : 'button',
								iconCls : 'downOne',
								tooltip : '下移',
								text : '下移',
								handler : me.downOne,// 向下移动一个
								scope : me
							}, {
								xtype : 'button',
								iconCls : 'downLast',
								tooltip : '置底',
								text : '置底',
								handler : me.downLast,// 置底
								scope : me
							}, '-', {
								xtype : 'button',
								iconCls : 'delete',
								text : '删除',
								handler : me.del,
								scope : me
							}, '-', {
								xtype : 'button',
								iconCls : 'delete',
								text : '清空',
								handler : me.cls,
								scope : me
							} ]
						});
						me.items = [ {
							xtype : 'form',
							fileUpload : true,
							bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
							defaults : {
								labelWidth : 40,
								allowBlank : false
							},
							items : [ {
								xtype : 'multifilesfield',
								fieldLabel : '文件',
								name : 'documents',
								listeners : {
									change : me.change
								}
							}, panel ]
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
						app.view.media.LocalUploadWindow.superclass.initComponent
								.apply(this, arguments);
					},
					change : function(filelist) {
						var win = this.findParentByType();
						win.datas = [];
						var data = [];
						var fileStore = win.findByType('grid')[0].store;
						for ( var i = 0; i < filelist.length; i++) {
							var files = filelist[i];
							if (files.length != 0) {
								for ( var j = 0; j < files.length; j++) {
									var lastModifiedDate = files[j].lastModifiedDate;
									var date = lastModifiedDate
											.format('Y-m-d H:i:s');
									data = [ files[j].name, files[j].size,
											files[j].type, date ];
									win.datas.push(data);
									data = [];
								}
							}
						}
						fileStore.loadData(win.datas);
					},
					save : function(button) {
						var win = button.findParentByType();
						var form = win.findByType('form')[0];
						var wait = new app.system.WaitWindow();
						wait.show();

						var panel = win.findByType('grid')[0];
						var items = panel.store.data.items;
						var strs = '';
						for ( var i = 0; i < items.length; i++) {
							strs += items[i].data.name + ',';
						}

						form
								.getForm()
								.submit(
										{
											url : 'cn/wizool/bank/servlet/MediaServlet?method=saveDoc',
											method : 'POST',
											timeout : 300,
											params : {
												strs : strs,
												type : win.type
											},
											success : function(form, action) {
												var result = action.result;
												if (result) {
													if (result.success) {
														wait.close();
														Ext.example.msg("提示",
																"操作成功");
														win.close();
													} else {
														Ext.Msg.alert("提示",
																result.message);
														wait.close();
													}
												} else {
													wait.close();
													Ext.Msg.alert("提示",
															"没有返回信息！");
												}
											},
											failure : function(form, action) {
												var result = action.result;
												Ext.Msg.alert("提示",
														result.message);
												wait.close();
												win.close();
											}
										});
					},
					upFirst : function(button) {// 置顶
						var me = this;
						var datas = me.datas;
						var items = me.items.items[0].items.items;
						for ( var i = 0; i < items.length; i++) {
							if (items[i].view != null) {
								var model = items[i].getSelectionModel().selections.items;
								if (model.length == 1) {
									var d = model[0].data;
									var data = [ d.name, d.size, d.type,
											d.lastModifiedDate ];
									var index = me.getIndex(data);
									datas.splice(index, 1);
									var newDatas = datas;
									newDatas.unshift(data);
									items[i].view.ds.loadData(newDatas);
								} else {
									Ext.example.msg('提示', '请选中一项');
								}
							}
						}
					},
					upOne : function(button) {// 向上移动一个
						var me = this;
						var datas = me.datas;
						var items = me.items.items[0].items.items;
						for ( var i = 0; i < items.length; i++) {
							if (items[i].view != null) {
								var model = items[i].getSelectionModel().selections.items;
								if (model.length == 1) {
									var d = model[0].data;
									var data = [ d.name, d.size, d.type,
											d.lastModifiedDate ];
									var index = me.getIndex(data);
									if (index != 0) {
										var temp;
										temp = datas[index];
										datas[index] = datas[index - 1];
										datas[index - 1] = temp;
									}
									items[i].view.ds.loadData(datas);
								} else {
									Ext.example.msg('提示', '请选中一项');
								}
							}
						}
					},
					downLast : function(button) {// 置底
						var me = this;
						var datas = me.datas;
						var items = me.items.items[0].items.items;
						for ( var i = 0; i < items.length; i++) {
							if (items[i].view != null) {
								var model = items[i].getSelectionModel().selections.items;
								if (model.length == 1) {
									var d = model[0].data;
									var data = [ d.name, d.size, d.type,
											d.lastModifiedDate ];
									var index = me.getIndex(data);
									datas.splice(index, 1);
									var newDatas = datas;
									newDatas.push(data);
									items[i].view.ds.loadData(newDatas);
								} else {
									Ext.example.msg('提示', '请选中一项');
								}
							}
						}
					},
					downOne : function(button) {// 向下移动一个
						var me = this;
						var datas = me.datas;
						var items = me.items.items[0].items.items;
						for ( var i = 0; i < items.length; i++) {
							if (items[i].view != null) {
								var model = items[i].getSelectionModel().selections.items;
								if (model.length == 1) {
									var d = model[0].data;
									var data = [ d.name, d.size, d.type,
											d.lastModifiedDate ];
									var index = me.getIndex(data);
									if (index != datas.length) {
										var temp;
										temp = datas[index];
										datas[index] = datas[index + 1];
										datas[index + 1] = temp;
									}
									items[i].view.ds.loadData(datas);
								} else {
									Ext.example.msg('提示', '请选中一项');
								}
							}
						}
					},
					del : function(button) {
						var me = this;
						var datas = me.datas;
						var items = me.items.items[0].items.items;
						for ( var i = 0; i < items.length; i++) {
							if (items[i].view != null) {
								var model = items[i].getSelectionModel().selections.items;
								if (model.length == 1) {
									var d = model[0].data;
									var data = [ d.name, d.size, d.type,
											d.lastModifiedDate ];
									var index = me.getIndex(data);
									datas.splice(index, 1);
									items[i].view.ds.loadData(datas);
								} else {
									Ext.example.msg('提示', '请选中一项');
								}
							}
						}
					},
					cls : function() {
						var me = this;
						var datas = me.datas;
						var items = me.items.items[0].items.items;
						for ( var i = 0; i < items.length; i++) {
							if (items[i].view != null) {
								var model = items[i].getStore();
								model.removeAll();
							}
						}
					},
					getIndex : function(data) {
						var datas = this.datas;
						for ( var m = 0; m < datas.length; m++) {
							if (datas[m][0] == data[0]) {
								return m;
							}
						}
					}
				});