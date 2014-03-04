Ext.ns('app.view.system');
app.view.system.ConfigEditWindow = Ext.extend(Ext.Window, {
	title : '编辑利率牌',
	width : 490,
	buttonAlign : 'center',
	iconCls : 'config16',
	modal : true,
	initComponent : function() {
		var me = this;
		Ext.Ajax.request({
			url : 'cn/wizool/bank/servlet/ConfigServlet?method=getList',
			success : function(response) {
				var text = Ext.decode(response.responseText);
				var list = text.list;
				if (text.count > 0) {
					var tabpanel = me.findByType('tabpanel')[0];
					var tabitems = tabpanel.items.items;
					for ( var i = 0; i < tabitems.length; i++) {
						var numbitems = tabitems[i].items.items;
						for ( var j = 0; j < numbitems.length; j++) {
							for ( var n in list) {
								if (numbitems[j].name == n) {
									numbitems[j].setValue(list[n]);
								}
							}
						}
					}
				}
			}
		});

		me.items = [ {
			xtype : 'form',
			layout : 'fit',
			items : [ {
				xtype : 'tabpanel',
				height : 220,
				activeTab : 0,
				bodyStyle : 'padding:5px',
				items : [ {
					title : '定期整存整取',
					layout : 'form',
					bodyStyle : 'padding:5 5 5 10',
					defaults : {
						xtype : 'numberfield',
						decimalPrecision : 3,
						maxLength : 5,
						allowBlank : false,
						width : '180'
					},
					items : [ {
						fieldLabel : '三个月',
						name : 'regularThreeMonth'
					}, {
						fieldLabel : '六个月',
						name : 'regularSixMonth'
					}, {
						fieldLabel : '一年',
						name : 'regularOneYear'
					}, {
						fieldLabel : '两年',
						name : 'regularTwoYear'
					}, {
						fieldLabel : '三年',
						name : 'regularThreeYear'
					}, {
						fieldLabel : '五年',
						name : 'regularFiveYear'
					} ]
				}, {
					title : '零、整存取',
					layout : 'form',
					bodyStyle : 'padding:5 5 5 10',
					defaults : {
						xtype : 'numberfield',
						decimalPrecision : 3,
						maxLength : 5,
						allowBlank : false,
						width : '180'
					},
					items : [ {
						fieldLabel : '一年',
						name : 'accessOneYear'
					}, {
						fieldLabel : '三年',
						name : 'accessThreeYear'
					}, {
						fieldLabel : '五年',
						name : 'accessFiveYear'
					} ]
				}, {
					title : '教育储蓄',
					layout : 'form',
					bodyStyle : 'padding:5 5 5 10',
					defaults : {
						xtype : 'numberfield',
						decimalPrecision : 3,
						maxLength : 5,
						allowBlank : false,
						width : '180'
					},
					items : [ {
						fieldLabel : '一年',
						name : 'educationOneYear'
					}, {
						fieldLabel : '三年',
						name : 'educationThreeYear'
					}, {
						fieldLabel : '六年',
						name : 'educationSixYear'
					} ]
				}, {
					title : '短期贷款',
					bodyStyle : 'padding:5 5 5 10',
					layout : 'form',
					defaults : {
						xtype : 'numberfield',
						decimalPrecision : 3,
						allowBlank : false,
						maxLength : 5,
						width : '180'
					},
					items : [ {
						fieldLabel : '六个月以内',
						xtype : 'textfield',
						name : 'shortLoansThreeMonth'
					}, {
						fieldLabel : '六个月至一年',
						name : 'shortLoansSixMonth'
					} ]
				}, {
					title : '中长期贷款',
					layout : 'form',
					bodyStyle : 'padding:5 5 5 10',
					defaults : {
						xtype : 'numberfield',
						decimalPrecision : 3,
						maxLength : 5,
						allowBlank : false,
						width : '180'
					},
					items : [ {
						fieldLabel : '一年至三年(含)',
						name : 'longLoansOneToThree'
					}, {
						fieldLabel : '三年至五年(含)',
						name : 'longLoansThreeToFive'
					}, {
						fieldLabel : '五年以上(含)',
						name : 'longLoansFiveMore'
					} ]
				}, {
					title : '其他',
					layout : 'form',
					bodyStyle : 'padding:5 5 5 10',
					defaults : {
						xtype : 'numberfield',
						decimalPrecision : 3,
						maxLength : 5,
						allowBlank : false,
						width : '180'
					},
					items : [ {
						fieldLabel : '活期',
						name : 'current'
					}, {
						fieldLabel : '协定存款',
						name : 'agreementDeposit'
					}, {
						fieldLabel : '折扣',
						name : 'discount'
					}, {
						fieldLabel : '通知存款(一天)',
						name : 'noticeDepositOneDay'
					}, {
						fieldLabel : '通知存款(七天)',
						name : 'noticeDepositSevenDay'
					}, {
						fieldLabel : '间隔时间(秒)',
						name : 'interval'
					}, {
						fieldLabel : '播放频率(分钟)',
						name : 'frequency'
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
		app.view.system.ConfigEditWindow.superclass.initComponent.apply(this,
				arguments);
	},
	save : function() {
		var me = this;
		var form = me.findByType('form')[0];
		form.getForm().submit({
			url : 'cn/wizool/bank/servlet/ConfigServlet?method=create',
			method : 'POST',
			success : function(form, action) {
				var flag = action.result;
				if (flag) {
					Ext.example.msg("提示", "操作成功");
					me.close();
				} else {
					Ext.Msg.alert("提 示", "程序异常，请联系管理员！");
				}
			}
		});
	}
});