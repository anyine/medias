Ext.ns('app.view.train');
app.view.train.TrainEditWindow = Ext
		.extend(
				Ext.Window,
				{
					title : '编辑培训资料',
					width : 800,
					buttonAlign : 'center',
					iconCls : 'create',
					modal : true,
					datas : [],
					initComponent : function() {
						var me = this;
						var hourSt = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '0', '0' ], [ '1', '1' ], [ '2', '2' ],
									[ '3', '3' ], [ '4', '4' ], [ '5', '5' ],
									[ '6', '6' ], [ '7', '7' ], [ '8', '8' ],
									[ '9', '9' ], [ '10', '10' ] ]
						});
						var minuteSt = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '0', '0' ], [ '5', '5' ],
									[ '10', '10' ], [ '15', '15' ],
									[ '20', '20' ], [ '25', '25' ],
									[ '30', '30' ], [ '35', '35' ],
									[ '40', '40' ], [ '45', '45' ],
									[ '50', '50' ], [ '55', '55' ] ]
						});
						me.fileStore = new Ext.data.SimpleStore({
							fields : [ 'id', 'name', 'length', 'date',
									'surfix', 'type' ],
							data : []
						});
						me.ajax();
						var treepanel = new app.view.dept.DeptTreePanel(
								{
									title : '机器列表',
									height : 480,
									frame : true,
									dbcheck : true,
									autoScroll : true,
									containerScroll : true,
									deptIds : me.oldDeptIds,
									classify : 'taskSelectMachine',
									bodyStyle : 'height: 480px; border: 1px ;border-color: #99BBE8;'
								});
						var panel = new Ext.grid.GridPanel(
								{
									layout : 'fit',
									height : 377,
									anchor : '99%',
									frame : true,
									store : me.fileStore,
									columns : [ new Ext.grid.RowNumberer({
										width : 60,
										header : '序号'
									}), {
										header : '名称',
										dataIndex : 'name',
										width : 230
									}, {
										header : '大小(字节)',
										dataIndex : 'length',
										width : 100,
										align : 'right'
									}, {
										header : '修改时间',
										dataIndex : 'date',
										width : 180
									} ],
									tbar : [
											'->',
											{
												xtype : 'button',
												iconCls : 'create',
												text : '本地文件',
												handler : function(button) {
													var win = new app.view.media.LocalUploadWindow(
															{
																title : '本地文件',
																type : 'train'
															});
													win.show();
													win.on('beforeclose',
															me.ajax, me);
												}
											},
											{
												xtype : 'button',
												iconCls : 'delete',
												text : '服务器文件',
												handler : function(button) {
													var win = new app.view.fileshare.FileSelectWindow1(
															{
																title : '服务器文件',
																type : 'train'
															});
													win.show();
													win.on('beforeclose',
															me.ajax, me);
												}
											}, '-', {
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
											} ]
								});

						me.items = [ {
							xtype : 'form',
							frame : true,
							bodyStyle : 'padding: 10px; background-color: #DFE8F6; border-color: #DFE8F6;',
							items : [ {
								layout : 'column',
								height : 480,
								items : [
										{
											columnWidth : .28,
											layout : 'form',
											items : [ treepanel ]
										},
										{
											columnWidth : .72,
											layout : 'form',
											bodyStyle : 'padding-left:20px;',
											labelWidth : 80,
											items : [
													{
														xtype : 'textfield',
														fieldLabel : '任务名称',
														name : 'name',
														allowBlank : false,
														anchor : '99%'
													},
													{
														xtype : 'textfield',
														fieldLabel : '开始日期',
														name : 'startDate',
														anchor : '99%',
														allowBlank : true
													},
													{
														layout : 'column',
														labelWidth : 30,
														anchor : '99%',
														items : [
																{
																	columnWidth : .20,
																	layout : 'form',
																	items : [ {
																		xtype : 'label',
																		text : '播放时长:'
																	} ]
																},
																{
																	columnWidth : .40,
																	layout : 'form',
																	items : [ {
																		xtype : 'combo',
																		fieldLabel : '时',
																		store : hourSt,
																		allowBlank : false,
																		triggerAction : 'all',
																		valueField : 'name',
																		mode : 'local',
																		displayField : 'name',
																		name : 'h',
																		listeners : {
																			select : function(
																					a,
																					b) {
																				me
																						.findByType('hidden')[0]
																						.setValue(a
																								.getValue());
																			}
																		}
																	} ]
																},
																{
																	columnWidth : .40,
																	layout : 'form',
																	items : [ {
																		xtype : 'combo',
																		fieldLabel : '分',
																		store : minuteSt,
																		allowBlank : false,
																		triggerAction : 'all',
																		valueField : 'name',
																		mode : 'local',
																		displayField : 'name',
																		name : 'm',
																		listeners : {
																			select : function(
																					a,
																					b) {
																				me
																						.findByType('hidden')[1]
																						.setValue(a
																								.getValue());
																			}
																		}
																	} ]
																} ]
													},
													{
														xtype : 'checkbox',
														fieldLabel : '选择',
														boxLabel : '任务所包含文件是否立刻下发？',
														anchor : '99%',
														listeners : {
															check : function(
																	field,
																	checked) {
																me.checked = checked;
															}
														}
													}, panel, {
														xtype : 'hidden',
														name : 'h'
													}, {
														xtype : 'hidden',
														name : 'm'
													} ]
										} ]
							} ]
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
						me
								.on(
										'close',
										function() {
											Ext.Ajax
													.request({
														url : 'cn/wizool/bank/servlet/MediaServlet?method=removeSession',
														params : {
															type : 'train'
														},
														success : function(
																response) {
															var text = Ext
																	.decode(response.responseText);
															if (text.success) {
																me.close();
															}
														}
													});
										}, me);
						app.view.train.TrainEditWindow.superclass.initComponent
								.apply(this, arguments);
					},
					ajax : function() {
						var me = this;
						Ext.Ajax
								.request({
									url : 'cn/wizool/bank/servlet/MediaServlet?method=getDocmentList',
									params : {
										type : 'train'
									},
									success : function(response) {
										var text = Ext
												.decode(response.responseText);
										var flag = text.success;
										me.datas = [];
										var data = [];
										for ( var i = 0; i < text.length; i++) {
											data = [ text[i].id, text[i].name,
													text[i].length,
													text[i].date,
													text[i].surfix,
													text[i].type ];
											me.datas.push(data);
											data = [];
										}
										me.fileStore.loadData(me.datas);
									}
								});
					},
					save : function(button) {
						var win = button.findParentByType();
						var docIds = '';
						var docs = win.findByType('grid')[0].view.ds.data.items;
						for ( var i = 0; i < docs.length; i++) {
							docIds += docs[i].data.id + ',';
						}
						var deptIds = '';
						var treepanel = win.findByType('panel')[0].items.items[0].items.items[0];
						var checked = treepanel.getChecked();
						if (checked.length > 0) {
							for ( var i = 0; i < checked.length; i++) {
								if (checked[i].childNodes.length == 0) {
									deptIds += checked[i].id + ' ';
								}
							}
						}

						if (deptIds == '') {
							Ext.MessageBox
									.confirm('提示', '您没有选择机构，系统将默认下发给所有培训机',
											function(btn) {
												if (btn == 'yes') {
													win.saveTrain(win, deptIds,
															docIds);
												}
											});
						} else {
							win.saveTrain(win, deptIds, docIds);
						}
					},
					upFirst : function() {// 置顶
						var me = this;
						var id = me.getSelectDataId();
						me.saveOrderDatas(id, 'upFirist');
						me.recordSelectModel(id);
					},
					upOne : function() {// 向上移动一个
						var me = this;
						var id = me.getSelectDataId();
						me.saveOrderDatas(id, 'upOne');
						me.recordSelectModel(id);
					},
					downLast : function() {// 置底
						var me = this;
						var id = me.getSelectDataId();
						me.saveOrderDatas(id, 'downLast');
						me.recordSelectModel(id);
					},
					downOne : function() {// 向下移动一个
						var me = this;
						var id = me.getSelectDataId();
						me.saveOrderDatas(id, 'downOne');
						me.recordSelectModel(id);
					},
					del : function() {
						var me = this;
						var id = me.getSelectDataId();
						var index = me.getSelectIndex(id);
						var grid = me.findByType('grid')[0];
						me.saveOrderDatas(id, 'deleteOne');
						me.fileStore.on('load', function() {
							var model = grid.view.grid.getSelectionModel()
									.selectRange(index, index, false);
						}, this);
					},
					saveOrderDatas : function(selectId, moveType) {
						var me = this;
						Ext.Ajax
								.request({
									url : 'cn/wizool/bank/servlet/MediaServlet?method=saveOrderDatas',
									params : {
										type : 'train',
										id : selectId,
										moveType : moveType
									},
									success : function(response) {
										var t = response.responseText;
										var text = Ext.decode(t);
										var flag = text.success;
										var message = text.message;
										if (message == null) {
											me.ajax();
										} else {
											Ext.Msg.alert("提 示", message);
										}
									}
								});
					},
					getSelectIndex : function(selectedId) {
						var me = this;
						var datas = me.datas;
						var selectData = [];
						var grid = me.findByType('grid')[0];
						var models = grid.view.grid.store.data.items;
						if (models.length > 0) {
							for ( var i = 0; i < models.length; i++) {
								var id = models[i].data.id;
								if (selectedId == id) {
									var d = models[i].data;
									selectData = [ d.id, d.name, d.date,
											d.length ];
								}
							}

						}
						for ( var m = 0; m < datas.length; m++) {
							if (datas[m][0] == selectData[0]) {
								return m;
							}
						}
					},
					getSelectDataId : function() {
						var me = this;
						var grid = me.findByType('grid')[0];
						var model = grid.getSelectionModel().selections.items;
						if (model.length == 1) {
							var d = model[0].data;
							return d.id;
						} else {
							Ext.example.msg('提示', '请选中一项');
						}
					},
					saveTrain : function(win, deptIds, docIds) {
						var docgrid = win.findByType('grid')[0].store;
						var length = docgrid.totalLength;
						if (length != 0) {
							var flag = win.doSave(length, docgrid, win);
							if (flag) {
								win.saveTask(win, deptIds, docIds);
							} else {
								Ext.Msg
										.alert("提示",
												"任务只可包含同种类型文件，但除图片、视频类型可选择多个文件,其它类型文件只可选择一个文件！");
							}
						} else {
							Ext.Msg.alert("提示", "没有添加文件!");
						}
					},
					doSave : function(length, docgrid, win) {
						if (length == 1) {
							return true;
						} else {
							var type = docgrid.data.items[0].data.surfix;
							var isType = win.checkType(type);
							if (isType == null) {
								return false;
							} else {
								for (i = 1; i < docgrid.data.items.length; i++) {
									var nextType = docgrid.data.items[i].data.surfix;
									var isNextType = win.checkType(nextType);
									if (isType != isNextType) {
										return false;
									}
								}
								return true;
							}
						}
					},
					checkType : function(type) {
						var t = type.toLowerCase();
						if (t == "png" || t == "jpg" || t == "bmp"
								|| t == "jpeg" || t == "gif") {
							return "图片";
						} else if (t == "flv" || t == "avi" || t == "mp4"
								|| t == "rmvb" || t == "3gp" || t == "mpeg"
								|| t == "mpg" || t == "wmv" || t == "flv"
								|| t == "mg4" || t == "mov" || t == "mts"
								|| t == "dvd" || t == "vob" || t == "rm"
								|| t == "vcd" || t == "svcd" || t == "ogm"
								|| t == "f4v" || t == "mkv" || t == "asf") {
							return "视频";
						} else {
							return null;
						}
					},
					saveTask : function(win, deptIds, docIds) {
						var form = win.findByType('form')[0];
						form
								.getForm()
								.submit(
										{
											url : 'cn/wizool/bank/servlet/TrainServlet?method=edit',
											method : 'POST',
											timeout : 300,
											params : {
												docIds : docIds,
												deptIds : deptIds,
												checked : win.checked,
												oldId : win.oldId == null ? ''
														: win.oldId
											},
											success : function(form, action) {
												var result = action.result;
												if (result.success) {
													Ext.example.msg("提示",
															"操作成功");
												}
												win.store.load({
													params : {
														start : 0,
														limit : 25
													}
												});
												win.close();
											},
											failure : function(form, action) {
												console.log(action);
												var result = action.result;
												if (result) {
													Ext.Msg.alert("提示",
															result.message);
												}
											}
										});
					},
					recordSelectModel : function(sId) {
						var me = this;
						var grid = me.findByType('grid')[0];
						me.fileStore.on('load', function() {
							var index = me.getSelectIndex(sId);
							var model = grid.view.grid.getSelectionModel()
									.selectRange(index, index, false);
						}, this);
					}
				});