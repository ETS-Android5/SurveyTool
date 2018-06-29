/**
 * Created by Hesk on 14年7月7日.
 * with Android delegation and support
 */
var PageCController = {};
jQuery(function ($) {
    PageCController = function (form_el) {
        this.$selector = form_el;
        this.init();
        return this;
    };
    PageCController.prototype = {
        init: function () {
            console.log("init and loaded PageController");
            var d = this;
            d.$form = $("#" + d.$selector);
            if (d.$selector == "gform_16") {
                d.formcheck = 'input:radio[value=""]';
                d.$sign_location_1 = $(".sign_1 .targetfield", d.$form);
                d.$sign_location_2 = $(".sign_2 .targetfield", d.$form);
            }
            if (d.$selector == "gform_17") {
                d.formcheck = 'input:radio[value=""]';
                d.$sign_location_1 = $(".sign_1 .targetfield", d.$form);
            }
            d.check_keys = [];
            //    d.dump = new String("");
            d.dump = "";
            d.testOnSubmission = false;
        },
        get_required_field_keys: function () {
            var d = this,
                checksequence = $(".gfield_contains_required input", d.$form),
                out = true;
            checksequence.each(function () {
                var el = $(this);
                d.check_keys.push(el.attr("name"));
            });
            return _.uniq(d.check_keys);
        },
        check_for_required: function (json_output_for_check) {
            var d = this,
                keys = d.get_required_field_keys(),
                total_required = keys.length,
                passed = true;
            // console.log("required fields");
            // console.log(keys);
            $.each(json_output_for_check, function (key, value) {
                //  console.log("field " + key + " val: " + value);
                if (typeof (json_output_for_check[key]) === "string") {
                    if (_.contains(keys, key)) {
                        //   console.log("required field val: " + value);
                        /* if (value == "") {
                         passed = false;
                         console.log(key + " not pass");
                         }*/
                    }
                }
                //this is array and it goes to here
                if (typeof (json_output_for_check[key]) === "object") {
                }
            });
            var c = 0;
            $.each(json_output_for_check, function (key, value) {
                if (_.contains(keys, key)) {
                    c++;
                }
            });
            if (c < total_required)passed = false;
            return passed;
        },
        save: function () {
            var d = this;
            if (d.testOnSubmission) {
                d.fillPNG('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');
                d.fillEnd(1);
            } else {
                var save_data_objects = d.$form.serializeObject(),
                    save_data_serialized = d.$form.serialize(),
                    save_json = $.toJSON(save_data_objects);
                if (d.check_for_required(save_data_objects)) {
                    //  var ob = $.parseJSON(save_data);
                    var emptyEles = d.$form.clone().find(d.formcheck);
                    //   console.log(emptyEles);
                    var empty_counts = emptyEles.size();
                    //  console.log(empty_counts);
                    //console.log(ob);
                    console.log(save_data_objects);
                    /*    .remove()
                     .end().serialize();
                     */
                    if (typeof (onecall_data_support) == 'undefined') {
                        alert("there is no android interface implemented");
                    } else {
                        onecall_data_support.saveform(d.$selector, save_json);
                    }
                } else {
                    if (typeof (onecall_data_support) === 'undefined') {
                        alert("please complete the required fields");
                    } else {
                        onecall_data_support.notcompletedialog();
                    }
                }
            }
            return true;
        },
        checkComplete: function (signature_id) {
            var d = this;
            var save_data_objects = d.$form.serializeObject(),
                save_data_serialized = d.$form.serialize(),
                save_json = $.toJSON(save_data_objects);
            if (typeof (onecall_data_support) === 'undefined') {
                alert("there is no android interface implemented");
            } else {
                onecall_data_support.checkCompleteReturn(d.$selector, d.check_for_required(save_data_objects) ? "1" : "0", signature_id);
            }
        },
        fillEnd: function (intReturn) {
            //not in use
            var d = this,
                header_jpg = 'data:image/jpg;base64, ',
                header_png = 'data:image/png;base64, ',
                header_jpeg = 'data:image/jpeg;charset=utf-8;base64, ',
                tag = '<img width="auto" height="200px" src="' + header_jpeg + d.dump + '"/>' + d.dump;
            //android cannot load more than certain amount of the string length code
            if (intReturn == 1 || intReturn == 3)
                d.$sign_location_1.html(tag);
            if (intReturn == 2)
                d.$sign_location_2.html(tag);
            d.dump = "";
        },
        fillEndF: function (intReturn, filepath) {
            var d = this,
                tag = '<img width="auto" height="200px" src="' + filepath + '"/>'
            if (intReturn == 1 || intReturn == 3)
                d.$sign_location_1.html(tag);
            if (intReturn == 2)
                d.$sign_location_2.html(tag);
        },
        fillPNG: function (bit64) {
            var d = this;
            //   d.dump.concat(bit64);
            d.dump += bit64;
            //  d.$sign_location.html(d.dump + " with length of : " + d.dump.length);
            // console.log(d.dump);
        }
    }

    // console.log(PageController);
});