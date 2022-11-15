class State {

    constructor() {
        this.runningARecipe = false;
        this.listeners = [];
    }

    isRunningARecipe() {
        return this.runningARecipe;
    }

    startedRunningRecipe() {
        this.runningARecipe = true;
        // disable all other buttons
        console.log($("button"))
        this.notifyListeners();
    }

    completedRunningRecipe() {
        this.runningARecipe = false;
        this.notifyListeners();
    }

    notifyListeners() {

        this.listeners.forEach((func) => {
            func();
        });
    }

    registerListeners(func) {
        this.listeners.push(func)
    }
}

const state = new State();
/*
function runRecipe(element) {
    if (state.isRunningARecipe()) {
        return;
    }
    state.startedRunningRecipe();

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/spring-boot-upgrade",
        data: JSON.stringify({
            recipe: $(element).attr('recipe')
        }),
        dataType: "json",
        contentType: 'application/json',
        processData: false
    })
        .always(function () {
            state.completedRunningRecipe();
        });
}
*/
/**
 *
 */
function applyRecipes(btn) {

    const recipeName = $(btn).attr('recipe');

    if (state.isRunningARecipe()) {
        return;
    }

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/spring-boot-upgrade",
        contentType: 'application/json',
        data: JSON.stringify({
            recipes: [$(btn).attr('recipe')]
        }),
        beforeSend: function() {
            state.startedRunningRecipe();
        },
        error: function(e) {
            // mark red flashlights / play alarm sound
            console.log("Error while applying recipe: " + e)
        }
    })
        .done(function () {
            // var section = $(`.run-a-recipe[recipe='${recipeName}']`)
            $(`.run-a-recipe[recipe='${recipeName}']`).fadeOut( 1200, "linear", function() {
                $(`.run-a-recipe[recipe='${recipeName}']`)
                    .closest(".sect2")
                    .remove();
            });

        })
        .always(function () {
            state.completedRunningRecipe();
        });
}

$( document ).ajaxStart(function() {
    $( ".log" ).text( "Triggered ajaxStart handler." );
});


state.registerListeners(function () {

    changeAllRecipesButtonState();
    changeRecipeButtonState();
})

function changeRecipeButtonState() {

    state.isRunningARecipe() ? runRecipeDisabledState() : runRecipeNormalState();
}

function changeAllRecipesButtonState() {

    $(".run-all-recipe").html(
        state.isRunningARecipe() ? runAllRecipeInLoadingState :
            runAllRecipeInNormalState
    );
}

const runAllRecipeInLoadingState = '<button type="button" class="btn btn-primary" id="recipeButton" ' +
    'name="apply-all-recipes-button" action="apply-all-recipes" ' +
    'onclick="applyRecipes()" disabled>' +
    'Run All Recipes' +
    '  <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>\n' +
    '  <span class="visually-hidden">Loading...</span>' +
    '</button>';

const runAllRecipeInNormalState = '<button type="button" class="btn btn-primary" id="recipeButton" ' +
    'name="apply-all-recipes-button" action="apply-all-recipes" ' +
    'onclick="applyRecipes()">' +
    'Run All Recipes' +
    '</button>';

$(document).ready(function () {
    $(".run-all-recipe").html(runAllRecipeInNormalState);

    runRecipeNormalState();
});

const runRecipeNormalState = function () {
    $(".run-a-recipe").each(function () {
        const div = $(this);
        div.html('<button type="button" class="btn btn-primary" id="recipeButton" ' +
            'name="apply-recipe-button" recipe="' + div.attr('recipe') + '" ' +
            'onclick="applyRecipes(this)">' +
            'Run Recipe' +
            '</button>');
    });
}

const runRecipeDisabledState = function () {
    $(".run-a-recipe").each(function () {
        const div = $(this);
        div.html('<button type="button" class="btn btn-primary" id="recipeButton" ' +
            'name="apply-recipe-button" recipe="' + div.attr('recipe') + '" ' +
            'onclick="runRecipe(this)" disabled>' +
            'Run Recipe' +
            '  <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>\n' +
            '  <span class="visually-hidden">Loading...</span>' +
            '</button>');
    });
}
