function gsurveySetUpLikertFields(){
    if (jQuery("table.gsurvey-likert").length > 0) {

        jQuery('table.gsurvey-likert td').click(function(e) {

            var input;

            if(jQuery(e.target).is("td.gsurvey-likert-choice")){
                input = jQuery(this).find("input");
                if(jQuery(input).is(':disabled'))
                    return false;
                jQuery(input).prop("checked",true);
                jQuery(input).closest('tr').find(".gsurvey-likert-selected").removeClass("gsurvey-likert-selected");
                jQuery(input).parent().addClass("gsurvey-likert-selected");
                jQuery(input).trigger('click');
            }

        });

        // add a hover state
        jQuery("table.gsurvey-likert td").hover(function(e){
            if (jQuery(e.target).is("td.gsurvey-likert-choice-label") || jQuery(this).find("input").is(':disabled')) {
                return false;
            } else {
                jQuery(this).addClass("gsurvey-likert-hover");
            }

        }, function(e){
            if (jQuery(e.target).is("td.gsurvey-likert-choice-label") || jQuery(this).find("input").is(':disabled')){
                return false;
            } else {
                jQuery(this).removeClass("gsurvey-likert-hover");
            }

        });
    }
}

jQuery(document).ready(function() {

    /*--------- Rank  ---------*/
    function gsurveyRankUpdateRank(ulElement){
        var IDs = [];
        jQuery(ulElement).find("li").each(function(){ IDs.push(this.id); });
        gsurveyRankings[ulElement.id] = IDs;
        jQuery(ulElement).parent().find("#" + ulElement.id + "-hidden").val(gsurveyRankings[ulElement.id])
    }
    function gsurveyRankMoveChoice(ulNode, fromIndex, toIndex){
        var ulNodeId = jQuery(ulNode).attr("id");
        var value = gsurveyRankings[ulNodeId][fromIndex];

        //deleting from old position
        gsurveyRankings[ulNodeId].splice(fromIndex, 1);

        //inserting into new position
        gsurveyRankings[ulNodeId].splice(toIndex, 0, value);
        gsurveyRankUpdateRank(ulNode);
    }

    function gsurveySetUpRankSortable(){
        var $rankField= jQuery('.gsurvey-rank');
        if ($rankField.length > 0) {
            $rankField.sortable({
                axis: 'y',

                cursor: 'move',
                update: function(event, ui){
                    var fromIndex = ui.item.data("index");
                    var toIndex = ui.item.index();
                    gsurveyRankMoveChoice(this, fromIndex, toIndex);
                }
            });

            gsurveyRankings = {};

            jQuery('.gsurvey-rank').each(function(){
                gsurveyRankUpdateRank(this);

            });
        }
    }

    jQuery(document).bind('gform_page_loaded', function(event, form_id, current_page){
        gsurveySetUpRankSortable();
        gsurveySetUpLikertFields();
    });

    gsurveySetUpRankSortable();
    gsurveySetUpLikertFields();

});


