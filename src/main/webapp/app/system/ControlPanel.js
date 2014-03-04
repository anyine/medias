Ext.ns('app.system');
app.system.ControlPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					title : '实时监控',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/DepartmentServlet?method=getPc"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'machineName',
												'machineStatus',
												'currentTaskStatus', 'taskId',
												'downloadStatus', 'bootTime',
												'ip', 'relation' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								ip : me.ip,
								status : me.status == null ? '开机' : me.status,
								type : me.type,
								dept : me.dept,
								location : me.location,
								version : me.version,
								versionEnabled : me.versionEnabled,
								taskName : me.taskName,
								taskNameEnabled : me.taskNameEnabled,
								taskStatus : me.taskStatus,
								fileName : me.fileName,
								fileNameEnabled : me.fileNameEnabled
							});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 900000
							}
						});

						var st1 = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '广告', '广告' ], [ '培训', '培训' ],
									[ '全部', '全部' ] ]
						});
						var st2 = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '开机', '开机' ], [ '关机', '关机' ],
									[ '全部', '全部' ] ]
						});
						var st3 = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '峄城联社', '峄城联社' ], [ '市中联社', '市中联社' ],
									[ '恒泰合行', '恒泰合行' ],
									[ '办事处科技中心', '办事处科技中心' ],
									[ '滕州商行', '滕州商行' ], [ '山亭联社', '山亭联社' ],
									[ '台儿庄联社', '台儿庄联社' ], [ '全部', '全部' ] ]
						});
						var st4 = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '描述', '描述' ], [ '下载', '下载' ],
									[ '启动', '启动' ], [ '其他', '其他' ],
									[ '全部', '全部' ] ]
						});

						var type = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st1,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 60,
							editable : false,
							value : '全部',
							listeners : {
								select : function() {
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
						var status = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st2,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 60,
							editable : false,
							value : '开机',
							listeners : {
								select : function() {
									var cb2 = this;
									me.status = cb2.getRawValue();
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										var cb2 = this;
										me.status = cb2.getRawValue();
										me.query(me);
									}
								}
							}
						});
						var dept = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st3,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 95,
							editable : false,
							value : '全部',
							listeners : {
								select : function() {
									var cb2 = this;
									me.dept = cb2.getRawValue();
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										var cb2 = this;
										me.dept = cb2.getRawValue();
										me.query(me);
									}
								}
							}
						});
						var taskStatus = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st4,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 60,
							editable : false,
							value : '全部',
							listeners : {
								select : function() {
									var cb4 = this;
									me.taskStatus = cb4.getRawValue();
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										var cb4 = this;
										me.taskStatus = cb4.getRawValue();
										me.query(me);
									}
								}
							}
						});

						var location = new Ext.form.TextField({
							width : 60,
							listeners : {
								blur : function() {
									me.location = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.location = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var ip = new Ext.form.TextField({
							width : 60,
							listeners : {
								blur : function() {
									me.ip = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.ip = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var version = new Ext.form.TextField({
							width : 60,
							listeners : {
								blur : function() {
									me.version = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.version = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var versionEnabled = new Ext.form.Checkbox({
							boxLabel : '不等于',
							listeners : {
								check : function(field, checked) {
									me.versionEnabled = checked;
									me.query(me);
								}
							}
						});

						var taskName = new Ext.form.TextField({
							width : 60,
							listeners : {
								blur : function() {
									me.taskName = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.taskName = this.getValue();
										me.query(me);
									}
								}
							}
						});
						var taskNameEnabled = new Ext.form.Checkbox({
							boxLabel : '不等于',
							listeners : {
								check : function(field, checked) {
									me.taskNameEnabled = checked;
									me.query(me);
								}
							}
						});
						var fileName = new Ext.form.TextField({
							width : 60,
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
						var fileNameEnabled = new Ext.form.Checkbox({
							boxLabel : '不等于',
							listeners : {
								check : function(field, checked) {
									me.fileNameEnabled = checked;
									me.query(me);
								}
							}
						});
						var runnerAotu;
						var aotu = new Ext.form.Checkbox({
							boxLabel : '自动',
							listeners : {
								check : function(field, checked) {
									if (checked == false) {
										if (runnerAotu != null)
											window.clearInterval(runnerAotu);
									} else {
										runnerAotu = window.setInterval(
												function() {
													me.store.reload();
												}, 5000);
									}
								}
							}
						});
						var sm = new Ext.grid.CheckboxSelectionModel();
						me.sm = sm;
						me.columns = [ sm, new Ext.grid.RowNumberer({
							width : 40,
							header : '序号'
						}), {
							header : '网点名称',
							width : 180,
							dataIndex : 'machineName'
						}, {
							header : '机器状态',
							width : 145,
							dataIndex : 'machineStatus'
						}, {
							header : '当前任务状态',
							width : 220,
							dataIndex : 'currentTaskStatus'
						}, {
							header : '当前下载状态',
							width : 220,
							dataIndex : 'downloadStatus'
						}, {
							header : '开机时间',
							width : 130,
							dataIndex : 'bootTime'
						}, {
							header : 'IP',
							width : 95,
							dataIndex : 'ip'
						}, {
							header : '联系人',
							width : 260,
							dataIndex : 'relation'
						} ];
						Ext.applyIf(me, {
							bbar : new Ext.PagingToolbar({
								store : me.store,
								pageSize : 900000,
								emptyMsg : "没有可以显示的数据",
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
						// 机器控制
						var update = new Ext.Action({
							text : '更新程序',
							handler : me.update,
							iconCls : 'update',
							scope : me
						});
						var killpower = new Ext.Action({
							text : '关闭电脑',
							handler : me.killpower,
							iconCls : 'shutdown',
							scope : me
						});
						var restart = new Ext.Action({
							text : '重启电脑',
							handler : me.restart,
							iconCls : 'restart',
							scope : me
						});
						// 机器信息
						var existtask = new Ext.Action({
							text : '播放任务',
							handler : me.existtask,
							iconCls : 'startsnap',
							scope : me
						});
						var killtask = new Ext.Action({
							text : '结束任务',
							handler : me.killtask,
							iconCls : 'killtask',
							scope : me
						});
						var compInfo = new Ext.Action({
							text : '机器信息',
							handler : me.compInfo,
							iconCls : 'cominfo',
							scope : me
						});
						var taskInfo = new Ext.Action({
							text : '任务信息',
							handler : me.taskInfo,
							iconCls : 'taskinfo',
							scope : me
						});
						var screenSave = new Ext.Action({
							text : '机器截屏',
							handler : me.screenSave,
							iconCls : 'screensave',
							scope : me
						});
						var sendCommand = new Ext.Action({
							text : '下发指令',
							handler : me.sendCommand,
							iconCls : 'sendcommand',
							scope : me
						});
						var uploadFile = new Ext.Action({
							text : '文件上传',
							handler : me.uploadFile,
							iconCls : 'uploadfile',
							scope : me
						});
						me.tbar = [
								{
									xtype : 'label',
									text : '联社:'
								},
								dept,
								'-',
								{
									xtype : 'label',
									text : '网点:'
								},
								location,
								'-',
								{
									xtype : 'label',
									text : '类型:'
								},
								type,
								'-',
								{
									xtype : 'label',
									text : '开关机:'
								},
								status,
								'-',
								{
									xtype : 'label',
									text : 'ip:'
								},
								ip,
								'-',
								{
									xtype : 'label',
									text : '版本:'
								},
								version,
								versionEnabled,
								'-',
								{
									xtype : 'label',
									text : '任务:'
								},
								taskName,
								taskNameEnabled,
								'-',
								taskStatus,
								'-',
								{
									xtype : 'label',
									text : '文件:'
								},
								fileName,
								fileNameEnabled,
								'->',
								'-',
								{
									xtype : 'button',
									text : '刷新',
									handler : me.query,
									iconCls : 'refresh',
									scope : me
								},
								aotu,
								'-',
								{
									text : '控制',
									iconCls : 'control16',
									menu : [ existtask, killtask, '-',
											taskInfo, compInfo, screenSave,
											'-', sendCommand, uploadFile, '-',
											update, restart, killpower ]
								} ];

						me.on('rowcontextmenu', me.rowcontextmenu);
						app.system.ControlPanel.superclass.initComponent.apply(
								this, arguments);
					},
					rowcontextmenu : function(grid, rowindex, e) {
						var me = this;
						// 点击右键同时选中该项
						me.colModel.config[0].selectRow(rowindex);
						e.preventDefault();

						var treeMenu = new Ext.menu.Menu([ {
							text : '任务信息',
							pressed : true,
							iconCls : 'taskinfo',
							handler : me.taskInfo,
							scope : me
						}, {
							text : '机器信息',
							pressed : true,
							handler : me.compInfo,
							iconCls : 'cominfo',
							scope : me
						}, {
							text : '机器截屏',
							pressed : true,
							handler : me.screenSave,
							iconCls : 'screensave',
							scope : me
						}, '-', {
							text : '下发指令',
							pressed : true,
							handler : me.sendCommand,
							iconCls : 'sendcommand',
							scope : me
						}, {
							text : '上传文件',
							pressed : true,
							handler : me.uploadFile,
							iconCls : 'uploadfile',
							scope : me
						}, '-', {
							text : '播放任务',
							pressed : true,
							handler : me.existtask,
							iconCls : 'startsnap',
							scope : me
						}, {
							text : '结束任务',
							pressed : true,
							handler : me.killtask,
							iconCls : 'killtask',
							scope : me
						}, '-', {
							text : '更新程序',
							pressed : true,
							handler : me.update,
							iconCls : 'update',
							scope : me
						}, {
							text : '关闭电脑',
							pressed : true,
							handler : me.killpower,
							iconCls : 'shutdown',
							scope : me
						}, {
							text : '重启电脑',
							pressed : true,
							handler : me.restart,
							iconCls : 'restart',
							scope : me
						} ]);
						// 定位菜单的显示位置e.getXY()
						treeMenu.showAt(e.getPoint());
					},
					compInfo : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length == 1) {
							var win = new app.view.system.CompInfoWindow({
								width : 600,
								height : 480,
								deptId : model[0].get('id')
							});
							win.show();
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					screenSave : function() {
						var me = this;
						var model = this.getSelectionModel().selections.items;
						if (model.length == 1) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认对机器截屏？',
											function(btn) {
												if (btn == 'yes') {
													window.location.href = "/bank/cn/wizool/bank/servlet/InterfaceServlet?method=downFile&list="
															+ model[0]
																	.get('id')
															+ ';S:screenSave';
												}
											})
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					sendCommand : function() {
						var me = this;
						me.createEditWindow(me, 'sendCommand');
					},
					uploadFile : function() {
						var me = this;
						me.createEditWindow(me, 'uploadFile');
					},
					createEditWindow : function(me, type) {
						var model = me.getSelectionModel().selections.items;
						if (model.length > 0) {
							var win = new app.view.system.EditCommandWindow({
								title : type == 'sendCommand' ? '编辑指令'
										: '编辑文件路径',
								iconCls : 'sendcommand',
								type : type,
								machineIds : function() {
									var strArray = [];
									for ( var i = 0; i < model.length; i++) {
										strArray.push(model[i].get('id'));
									}
									return strArray;
								}(),
							});
							win.show();
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					taskInfo : function() {
						var me = this;
						var node = me.getSelectionModel().selections.items;
						if (node.length == 1) {
							if (node[0].get('taskId') != '') {
								Ext.Ajax
										.request({
											url : 'cn/wizool/bank/servlet/MediaServlet?method=clickModify',
											params : {
												taskId : node[0].get('taskId'),
												type : 'media'
											},
											success : function(response) {
												var text = Ext
														.decode(response.responseText);
												var win = new app.view.media.MediaEditWindow(
														{
															title : '查看任务详细信息',
															oldId : text.oldId,
															store : me.store,
															oldDocIds : text.docIds,
															oldDeptIds : text.deptIds,
															deptIds : text.deptIds,
															hidden : true,
														});
												var typeSt = new Ext.data.SimpleStore(
														{
															fields : [ 'id',
																	'name' ],
															data : [
																	[ '开', '开' ],
																	[ '关', '关' ] ]
														});
												var form = win
														.findByType('form')[0].items.items[0].items.items[1];
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
													Ext
															.getCmp("date01")
															.setValue(
																	text.startDate);
													Ext
															.getCmp("date02")
															.setValue(
																	text.endDate);
													Ext.getCmp("sjd").setValue(
															true);
												} else {
													Ext
															.getCmp("date01")
															.setValue(
																	text.startDate);
													Ext.getCmp("myt").setValue(
															true);
												}

												win.findByType('numberfield')[0]
														.setValue(text.hour);
												win.findByType('numberfield')[1]
														.setValue(text.minute);
											}
										});

								// var win = new
								// app.view.media.SelectInfoWindow({
								// width : 600,
								// height : 480,
								// taskId : node[0].get('taskId')
								// });
								// win.show();
							} else {
								Ext.Msg.alert('提示', '此选项没有播放任务');
							}
						} else {
							Ext.Msg.alert('提示', '请选择一项');
						}
					},
					existtask : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length != 0) {
							var strArray = [];
							for ( var i = 0; i < model.length; i++) {
								strArray.push(model[i].get('id'));
							}

							var win = new app.system.ExistTaskWindow({
								width : 750,
								height : 450,
								title : '已有任务',
								iconCls : 'existtask',
								depts : strArray
							});
							win.show();
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					killtask : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length != 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认结束任务？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
																params : {
																	list : function() {
																		var strArray = [];
																		for ( var i = 0; i < model.length; i++) {
																			strArray
																					.push(model[i]
																							.get('id')
																							+ ';K:'
																							+ model[i]
																									.get('taskId'));
																		}
																		return strArray;
																	}()
																},
																success : function(
																		result) {
																	me.store
																			.reload();
																}
															});
												}
											})
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					killpower : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length != 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认关机？',
											function(btn) {
												if (btn == 'yes') {
													for ( var i = 0; i < model.length; i++) {
														Ext.Ajax
																.request({
																	url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
																	params : {
																		list : function() {
																			var strArray = [];
																			for ( var i = 0; i < model.length; i++) {
																				strArray
																						.push(model[i]
																								.get('id')
																								+ ';S:f6f2a76c-bf34-4cc7-adba-8d2ab99d0fb8');
																			}
																			return strArray;
																		}()
																	},
																	success : function(
																			result) {
																	}
																});
													}
												}
											})
						} else {
							Ext.Msg.alert("提示", "请选择一项");
						}
					},
					query : function() {
						var me = this;
						me.store.reload();
					},
					update : function() {
						var me = this;
						var model = this.getSelectionModel().selections.items;
						if (model.length != 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认对机器更新程序？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
																params : {
																	list : function() {
																		var strArray = [];
																		for ( var i = 0; i < model.length; i++) {
																			strArray
																					.push(model[i]
																							.get('id')
																							+ ';S:updateProgram');
																		}
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
					},
					restart : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length != 0) {
							Ext.Msg
									.confirm(
											'提示窗口？',
											'确认重新启动机器？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/InterfaceServlet?method=putTasks',
																params : {
																	list : function() {
																		var strArray = [];
																		for ( var i = 0; i < model.length; i++) {
																			strArray
																					.push(model[i]
																							.get('id')
																							+ ';S:f6f2a76c-bf34-4cc7-adba-8d2ab99d0fb9');
																		}
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