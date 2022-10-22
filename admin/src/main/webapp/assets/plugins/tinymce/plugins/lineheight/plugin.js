(function () {
var lineheight = (function () {
  'use strict';

  var global = tinymce.util.Tools.resolve('tinymce.PluginManager');

  var applyFormat = function (editor, format, value) {
    editor.undoManager.transact(function () {
      editor.focus();
      editor.formatter.apply(format, value);
      editor.nodeChanged();
    });
  };
  var setup = function (editor) {
    editor.on('init', function () {
      editor.formatter.register({
        lineheight: {
          inline: 'span',
          styles: { 'line-height': '%value' }
        }
      });
    });
  };
  var $_dqduretwjn6umnse = {
    applyFormat: applyFormat,
    setup: setup
  };

  var register = function (editor) {
    editor.addCommand('ApplyLineHeight', function (format, value) {
      $_dqduretwjn6umnse.applyFormat(editor, format, value);
    });
  };
  var $_8nx61atvjn6umnsd = { register: register };

  var getSelectedLineHeight = function (editor) {
    var node = editor.dom.getParent(editor.selection.getNode(), 'span');
    return editor.dom.getStyle(node, 'line-height') || '';
  };
  var listState = function (editor, items) {
    return function (e) {
      editor.on('NodeChange', function (e) {
        var formatter = editor.formatter;
        var value = null;
        e.parents.forEach(function (node) {
          items.forEach(function (item) {
            if (formatter.matchNode(node, 'lineheight', { value: item.value })) {
              value = item.value;
            }
            if (value) {
              return false;
            }
          });
        });
      });
    };
  };
  var updateSelection = function (editor) {
    return function (e) {
      var lineheight = getSelectedLineHeight(editor);
      if (!!lineheight) {
        e.control.items().each(function (ctrl) {
          ctrl.active(ctrl.settings.value === lineheight);
        });
      } else {
        e.control.items().each(function (ctrl) {
          ctrl.active(false);
        });
        e.control.items().each(function (ctrl) {
          if (ctrl.settings.value === 'inherit') {
            ctrl.active(true);
            return false;
          }
        });
      }
    };
  };
  var register$1 = function (editor) {
    editor.addButton('lineheight', function () {
      var items = [], defaultLineHeightFormats = 'Default=inherit 1.2=1.2em 1.4=1.4em 1.5=1.5em 1.75=1.75em 2=2em 2.5=2.5em 3=3em 4=4em 5=5em';
      var lineheight_formats = editor.settings.lineheight_formats || defaultLineHeightFormats;
      lineheight_formats = lineheight_formats.startsWith('Default=inherit') ? lineheight_formats : 'Default=inherit ' + lineheight_formats;
      lineheight_formats.split(' ').forEach(function (item) {
        var text = item, value = item;
        var values = item.split('=');
        if (values.length > 1) {
          text = values[0];
          value = values[1];
        }
        items.push({
          text: text,
          value: value
        });
      });
      return {
        type: 'menubutton',
        icon: 'text-height',
        tooltip: 'Line Height',
        menu: items,
        onPostRender: listState(editor, items),
        onshow: updateSelection(editor),
        onselect: function (e) {
          editor.execCommand('ApplyLineHeight', 'lineheight', { value: e.control.settings.value });
        }
      };
    });
  };
  var $_1ot724txjn6umnsf = { register: register$1 };

  global.add('lineheight', function (editor) {
    $_dqduretwjn6umnse.setup(editor);
    $_8nx61atvjn6umnsd.register(editor);
    $_1ot724txjn6umnsf.register(editor);
  });
  function Plugin () {
  }

  return Plugin;

}());
})();
