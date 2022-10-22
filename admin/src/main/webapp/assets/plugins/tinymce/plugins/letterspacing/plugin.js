(function () {
var letterspacing = (function () {
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
        letterspacing: {
          inline: 'span',
          styles: { 'letter-spacing': '%value' }
        }
      });
    });
  };
  var $_fzus05u0jn6umnsr = {
    applyFormat: applyFormat,
    setup: setup
  };

  var getSelectedLetterSpacing = function (editor) {
    var node = editor.dom.getParent(editor.selection.getNode(), 'span');
    return editor.dom.getStyle(node, 'letter-spacing') || '';
  };
  var listState = function (editor, items) {
    return function (e) {
      editor.on('NodeChange', function (e) {
        var formatter = editor.formatter;
        var value = null;
        e.parents.forEach(function (node) {
          items.forEach(function (item) {
            if (formatter.matchNode(node, 'letterspacing', { value: item.value })) {
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
      var letterspacing = getSelectedLetterSpacing(editor);
      if (!!letterspacing) {
        e.control.items().each(function (ctrl) {
          ctrl.active(ctrl.settings.value === letterspacing);
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
  var register = function (editor) {
    editor.addButton('letterspacing', function () {
      var items = [], defaultLetterSpactingFormats = 'Default=inherit 0.25=0.25px 0.5=0.5px 1=1px 1.5=1.5px 2=2px 2.5=2.5px 3=3px 4=4px 5=5px';
      var letterspacing_formats = editor.settings.letterspacing_formats || defaultLetterSpactingFormats;
      letterspacing_formats = letterspacing_formats.startsWith('Default=inherit') ? letterspacing_formats : 'Default=inherit ' + letterspacing_formats;
      letterspacing_formats.split(' ').forEach(function (item) {
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
        icon: 'text-width',
        tooltip: 'Letter Spacing',
        menu: items,
        onPostRender: listState(editor, items),
        onshow: updateSelection(editor),
        onselect: function (e) {
          editor.execCommand('ApplyLetterSpacing', 'letterspacing', { value: e.control.settings.value });
        }
      };
    });
  };
  var $_4vz5c3u1jn6umnst = { register: register };

  var register$1 = function (editor) {
    editor.addCommand('ApplyLetterSpacing', function (format, value) {
      $_fzus05u0jn6umnsr.applyFormat(editor, format, value);
    });
  };
  var $_3entf0u2jn6umnsv = { register: register$1 };

  global.add('letterspacing', function (editor) {
    $_fzus05u0jn6umnsr.setup(editor);
    $_3entf0u2jn6umnsv.register(editor);
    $_4vz5c3u1jn6umnst.register(editor);
  });
  function Plugin () {
  }

  return Plugin;

}());
})();
