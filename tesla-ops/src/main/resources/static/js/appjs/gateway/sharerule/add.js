var prefix = "/filter/sharerule";
$(document).ready(function() {
  pageSetUp();
  var pagefunction = function() {
    $("button[id^='submit']").each(function(index, element) {
      $(this).click(function() {
        var submitId = this.id;
        var formId = 'ruleForm_' + submitId.split('_')[1];
        $("#" + formId).validate({
          rules: {
            rule: {
              required: true
            },
            url: {
              required: true
            }
          },
          messages: {
            rule: {
              required: "请输入规则"
            },
            url: {
              required: "请输入匹配URL"
            }
          },
          submitHandler: function(form) {
            $(form).ajaxSubmit({
              cache: true,
              type: "post",
              url: prefix + "/save",
              data: $("#" + formId).serialize(),
              async: false,
              success: function() {
                $("#" + formId).addClass('submited');
                loadURL(prefix, $('#content'));
              }
            });
          },
          errorPlacement: function(error, element) {
            error.insertAfter(element.parent());
          }
        });
        $("#" + formId).submit();
      })
    });
  }
  var acefunction = function() {
    $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
      var activetab = $(e.target).attr("aria-controls")
      $("#" + activetab).find("textarea").ace({
        theme: 'idle_fingers',
        lang: 'text'
      });
    })
    $("#URLParamHttpRequestFilter").find("textarea").ace({
      theme: 'idle_fingers',
      lang: 'text'
    });
  }
  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
  acefunction();
});
