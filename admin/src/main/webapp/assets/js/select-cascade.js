/**
 * 实现功能：多个select下拉框级联
 * 具体用法可参考系统-区县管理
 */
$(function () {
	$.fn.selectCascade = function(options, param){
		if (typeof options == 'string'){
			return $.fn.selectCascade.methods[options](this, param);
		}
		
		options = options || {};
		
		var _this = $(this);
		options = $.extend({}, options, {
			value : _this.data('current-value')
		});
		
		return this.each(function(){
			var state = $.data(this, 'selectCascade');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'selectCascade', {
					options: $.extend({}, $.fn.selectCascade.defaults, options)
				});
			}
			
			_this.on("change",function (event){
				clearOption(state.options);
				var thisValue = event.target.value;
				if(!thisValue || thisValue == ''){
					return;
				}
				var cascadeChildId = state.options.cascadeChildId;
				if(cascadeChildId){
					var _child = $('#'+cascadeChildId);
					var childOptions = _child.selectCascade('options');
					var params = childOptions.beforeLoad(thisValue);
					request(_child, childOptions, params);
				}
				event.preventDefault();
			});
			
			var cascadeParentId = state.options.cascadeParentId;
			if(cascadeParentId){
				var _parent = $('#'+cascadeParentId);
				var parentOptions = _parent.selectCascade('options');
				var value = parentOptions.value;
				if(value != undefined && value != ''){
					var params = state.options.beforeLoad(value);
					request(_this, state.options, params);
				}
			} else {
				request(_this, state.options, {});
			}
		});
   	}
	
	function clearOption(options){
		var cascadeChildId = options.cascadeChildId;
		if(cascadeChildId){
			var _child = $('#'+cascadeChildId);
			var childOptions = _child.selectCascade('options');
			clearOption(childOptions);
			_child.html('');
		}
	}
	
	function request(target, options, param){
		param = $.extend({}, options.queryParams, param || {});
		if (!options.url){
			console.log('selectCascade的参数url不能为空');
			return false;
		}
		
		doPost(options.url, param, function(data){
        	var html = '';
        	var firstOption = options.firstOption;
        	if(!firstOption){
        		html += '<option value="" style="display: none;" disabled selected></option>';
        	} else {
        		html += '<option value="'+firstOption.valueField+'">'+firstOption.textField+'</option>';
        	}
        	data = options.loadFilter(data);
        	$.each(data,function(index,item){
        		var value = item[options.valueField];
        		var text = item[options.textField];
        		html += '<option value="'+value+'"'+(options.value != undefined && options.value == item.id ? ' selected' : '')+'>'+text+'</option>';
        	});
        	target.html(html);
        	options.afterSetOption(target);
	    });
	}
	
	$.fn.selectCascade.methods = {
		options: function(jq){
			return $.data(jq[0], 'selectCascade').options;
		}
	};
	
	$.fn.selectCascade.defaults = {
		valueField: 'id',
		textField: 'name',
		url: null,
		queryParams: {},
		cascadeChildId : null,
		cascadeParentId : null,
		firstOption: null,
		value : null,
		loadFilter: function(data){
			return data.data;
		},
		beforeLoad: function(parentValue){
			return {};
		},
		afterSetOption: function(_this){}
	};
});