Ext.ns('app.view.media');
app.view.media.SelectDocsGridPanel = Ext
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
												url : "cn/wizool/bank/servlet/MediaServlet?method=getSelectDocsInfo&taskId="
														+ me.taskId
											}),
									reader : new Ext.data.JsonReader({
										totalProperty : "total",
										root : 'root',
										fields : [ 'id', 'name', 'length',
												'uploadDate', 'dept' ]
									})
								});
						me.store = store;
						me.store.load();
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
							width : 80
						}, {
							header : '上传日期',
							dataIndex : 'uploadDate',
							width : 140
						}, {
							header : '上传联社',
							dataIndex : 'dept',
							width : 100
						} ];
						app.view.media.SelectDeptsGridPanel.superclass.initComponent
								.apply(this, arguments);
					}
				});
