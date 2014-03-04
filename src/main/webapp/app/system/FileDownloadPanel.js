Ext.ns('app.system');
app.system.FileDownloadPanel = Ext
		.extend(
				Ext.grid.GridPanel,
				{
					title : '下载进度',
					initComponent : function() {
						var me = this;
						var store = new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : "cn/wizool/bank/servlet/FileDownloadServlet?method=getAll"
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'dept',
												'location', 'start', 'docName',
												'type', 'length', 'end' ]
									})
								});
						me.store = store;
						me.store.on('beforeload', function(thiz) {
							Ext.apply(thiz.baseParams, {
								type : me.type,
								dept : me.dept,
								docName : me.docName,
								location : me.location
							});
						});
						me.store.load({
							params : {
								start : 0,
								limit : 25
							}
						});

						var st1 = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '峄城联社', '峄城联社' ], [ '市中联社', '市中联社' ],
									[ '恒泰合行', '恒泰合行' ],
									[ '办事处科技中心', '办事处科技中心' ],
									[ '滕州商行', '滕州商行' ], [ '山亭联社', '山亭联社' ],
									[ '台儿庄联社', '台儿庄联社' ], [ '全部', '全部' ] ]
						});
						var cb1 = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st1,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 100,
							editable : false,
							listeners : {
								select : function() {
									var cb1 = this;
									var res = cb1.getRawValue();
									if (res == "全部") {
										me.dept = ""
									} else {
										me.dept = res;
									}
									me.query(me);
								},
								blur : function() {
									var cb1 = this;
									var res = cb1.getRawValue();
									if (res == "全部") {
										me.dept = ""
									} else {
										me.dept = res;
									}
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										var cb1 = this;
										var res = cb1.getRawValue();
										if (res == "全部") {
											me.dept = ""
										} else {
											me.dept = res;
										}
										me.query(me);
									}
								}
							}
						});
						var st2 = new Ext.data.SimpleStore({
							fields : [ 'id', 'name' ],
							data : [ [ '广告', '广告' ], [ '培训', '培训' ],
									[ '全部', '全部' ] ]
						});
						var cb2 = new Ext.form.ComboBox({
							xtype : 'combo',
							store : st2,
							triggerAction : 'all',
							valueField : 'name',
							mode : 'local',
							displayField : 'name',
							width : 80,
							editable : false,
							listeners : {
								select : function() {
									var cb2 = this;
									me.type = cb2.getRawValue();
									me.query(me);
								},
								blur : function() {
									var cb2 = this;
									me.type = cb2.getRawValue();
									me.query(me);
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.query(me);
									}
								}
							}
						});
						var location = new Ext.form.TextField({
							width : 80,
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
						var docName = new Ext.form.TextField({
							width : 80,
							listeners : {
								blur : function() {
									me.docName = this.getValue();
								},
								specialkey : function(field, e) {
									if (e.getKey() == Ext.EventObject.ENTER) {
										me.docName = this.getValue();
										me.query(me);
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
							header : '机构名称',
							width : 100,
							dataIndex : 'dept'
						}, {
							header : '机器名称',
							width : 110,
							dataIndex : 'location'
						}, {
							header : '设备名称',
							width : 80,
							dataIndex : 'name'
						}, {
							header : '设备类型',
							width : 70,
							dataIndex : 'type'
						}, {
							header : '文件名称',
							width : 180,
							dataIndex : 'docName'
						}, {
							header : '文件大小',
							width : 80,
							align : 'right',
							dataIndex : 'length'
						}, {
							header : '开始时间',
							width : 150,
							dataIndex : 'start'
						}, {
							header : '完成时间',
							width : 150,
							dataIndex : 'end'
						} ];
						Ext.applyIf(me, {
							bbar : new Ext.PagingToolbar({
								store : store,
								pageSize : 25,
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
						var deleteById = new Ext.Action({
							text : "删除选中项",
							handler : me.del,
							iconCls : "delete",
							scope : me
						});
						var deleteAll = new Ext.Action({
							text : "删除全部",
							handler : me.deleteAll,
							iconCls : "delete",
							scope : me
						});
						me.tbar = [ {
							xtype : 'label',
							text : '联社:'
						}, cb1, '-', {
							xtype : 'label',
							text : '网点:'
						}, location, '-', {
							xtype : 'label',
							text : '类型:'
						}, cb2, '-', {
							xtype : 'label',
							text : '文件名称:'
						}, docName, '-', {
							xtype : 'button',
							text : '查询',
							iconCls : 'query',
							handler : me.query,
							scope : me
						}, '->', '-', {
							xtype : 'button',
							text : '刷新',
							handler : me.query,
							iconCls : 'refresh',
							scope : me
						}, '-', {
							text : '删除',
							menu : [ deleteById, deleteAll ]
						} ]
						app.system.FileDownloadPanel.superclass.initComponent
								.apply(this, arguments);
					},
					query : function() {
						var me = this;
						me.store.reload();
					},
					deleteAll : function() {
						var me = this;
						Ext.Msg
								.confirm(
										"提示",
										"确定全部删除吗？",
										function(btn) {
											if (btn == "yes") {
												Ext.Ajax
														.request({
															url : 'cn/wizool/bank/servlet/FileDownloadServlet',
															params : {
																method : 'deleteFileDownload',
																style : 'deleteAll',
																type : me.type,
																dept : me.dept,
																docName : me.docName,
																location : me.location
															},
															success : function(
																	response) {
																var text = Ext
																		.decode(response.responseText);
																if (text.success) {
																	Ext.example
																			.msg(
																					"提示",
																					"操作成功");
																	me.store
																			.reload();
																} else {
																	Ext.Msg
																			.alert(
																					"提示",
																					text.message);
																}
															}
														});
											}
										}, this);
					},
					del : function() {
						var me = this;
						var model = me.getSelectionModel().selections.items;
						if (model.length > 0) {
							Ext.Msg
									.confirm(
											'提示窗口',
											'确认要删除下载历史？',
											function(btn) {
												if (btn == 'yes') {
													Ext.Ajax
															.request({
																url : 'cn/wizool/bank/servlet/FileDownloadServlet',
																params : {
																	method : 'deleteFileDownload',
																	style : 'deleteSelected',
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
																				.reload();
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
				})