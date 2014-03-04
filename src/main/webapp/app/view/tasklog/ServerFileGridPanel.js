Ext.ns('app.view.tasklog');
app.view.tasklog.ServerFileGridPanel = Ext
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
												url : "cn/wizool/bank/servlet/TaskLogServlet?method=getServerFiles"
											}),
									reader : new Ext.data.JsonReader({
										root : 'root',
										totalProperty : 'total',
										fields : [ 'id', 'uploaddate',
												'length', 'name', 'type',
												'dept', 'user', 'state',
												'surfix', 'len' ]
									})
								});
						if (me.checked) {
							me
									.on(
											'dblclick',
											function() {
												var node = me
														.getSelectionModel().selections.items;
												var id = node[0].get('id');
												var surfix = node[0]
														.get('surfix');
												var length = node[0].get('len');
												window.location.href = 'app://open:fileId='
														+ id
														+ ';surfix='
														+ surfix
														+ ';length='
														+ length + ';';

											});
						}
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
							header : '上传机构',
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
						me.tbar = [ {
							xtype : 'label',
							text : '文件类型:'
						}, cb1, '-', {
							xtype : 'label',
							text : '文件名称:'
						}, name, '-', {
							xtype : 'button',
							text : '查询',
							iconCls : 'query',
							handler : function() {
								me.query(me);
							}
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
						app.view.tasklog.ServerFileGridPanel.superclass.initComponent
								.apply(this, arguments);
					},
					query : function() {
						var me = this;
						me.store.reload();
					}
				});