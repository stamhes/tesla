var prefix = "/filter/sharerule";
$(document).ready(function() {
  pageSetUp();
  var pagefunction = function() {
    var form = $("#ruleForm");
    var ace = form.find("textarea").data('ace').editor.ace;
    var value = ace.getValue();
    $("#rule").val(value);
    form.validate({
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
          data: $(form).serialize(),
          async: false,
          success: function() {
            $(form).addClass('submited');
            loadURL(prefix, $('#content'));
          }
        });
      },
      errorPlacement: function(error, element) {
        error.insertAfter(element.parent());
      }
    });

  };
  var swiperfunction = function() {
    new Swiper('#certify .swiper-container', {
      watchSlidesProgress: true,
      slidesPerView: 'auto',
      centeredSlides: true,
      loop: true,
      loopedSlides: 5,
      autoplay: true,
      navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
      },
      pagination: {
        el: '.swiper-pagination',
      },
      on: {
        progress: function(progress) {
          for (i = 0; i < this.slides.length; i++) {
            var slide = this.slides.eq(i);
            var slideProgress = this.slides[i].progress;
            modify = 1;
            if (Math.abs(slideProgress) > 1) {
              modify = (Math.abs(slideProgress) - 1) * 0.3 + 1;
            }
            translate = slideProgress * modify * 260 + 'px';
            scale = 1 - Math.abs(slideProgress) / 5;
            zIndex = 999 - Math.abs(Math.round(10 * slideProgress));
            slide.transform('translateX(' + translate + ') scale(' + scale + ')');
            slide.css('zIndex', zIndex);
            slide.css('opacity', 1);
            if (Math.abs(slideProgress) > 3) {
              slide.css('opacity', 0);
            }
          }
        },
        setTransition: function(transition) {
          for (var i = 0; i < this.slides.length; i++) {
            var slide = this.slides.eq(i)
            slide.transition(transition);
          }

        }
      }
    });
  };
  var choosefunction = function() {
    try {
      $(".choose").each(function() {
        $(this).bind("click", function() {
          var filter = $(this).data("compnent");
          $("#filterType").val(filter);
        });
      });
    } finally {
      $("#ruleForm").find("textarea").ace({
        theme: 'idle_fingers',
        lang: 'text'
      })
    }
  }
  loadScript("js/plugin/jquery-form/jquery-form.min.js", pagefunction);
  swiperfunction();
  choosefunction();
});
