Ext.ns('app.view.media');
app.view.media.SelectDeptsGridPanel = Ext
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
												url : "cn/wizool/bank/servlet/MediaServlet?method=getSelectDeptsInfo&taskId="
														+ me.taskId
											}),
									reader : new Ext.data.JsonReader(
											{
												totalProperty : "total",
												root : 'root',
												fields : [ 'id', 'name',
														'location', 'type',
														'dept', 'status', 'ip' ]
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
							header : '联社名称',
							dataIndex : 'dept',
							width : 140
						}, {
							header : '网点名称',
							dataIndex : 'location',
							width : 130
						}, {
							header : '设备名称',
							dataIndex : 'name',
							width : 60
						}, {
							header : '设备IP',
							dataIndex : 'ip',
							width : 90
						}, {
							header : '类型',
							dataIndex : 'type',
							width : 40
						}, {
							header : '开关机',
							dataIndex : 'status',
							width : 50
						} ];
						app.view.media.SelectDeptsGridPanel.superclass.initComponent
								.apply(this, arguments);
					}
				});
