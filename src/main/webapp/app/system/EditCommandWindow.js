Ext.ns('app.view.system');
app.view.system.EditCommandWindow = Ext
		.extend(
				Ext.Window,
				{
					title : '编辑',
					width : 490,
					buttonAlign : 'center',
					iconCls : 'create',
					modal : true,
					initComponent : function() {
						var me = this;
						var fieldLabel = me.type == 'sendCommand' ? '指令内容'
								: '文件路径';
						me.items = [ {
							xtype : 'form',
							bodyStyle : 'padding: 20px; background-color: #DFE8F6; border-color: #DFE8F6;',
							defaults : {
								labelWidth : 40,
								anchor : '97%'
							},
							items : [ {
								xtype : 'textfield',
								name : 'content',
								allowBlank : false,
								fieldLabel : fieldLabel
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
						app.view.system.EditCommandWindow.superclass.initComponent
								.apply(this, arguments);
					},
					save : function() {
						var me = this;
						var form = me.findByType('form')[0];
						var content = form.getForm().findField('content')
								.getValue();
						var type = me.type == 'sendCommand' ? ';C:' : ';U:';
						var machineIds = me.machineIds;

						window.location.href = "/bank/cn/wizool/bank/servlet/InterfaceServlet?method=downFile&list="
								+ machineIds[0] + type + content;
						me.close();
					}
				});