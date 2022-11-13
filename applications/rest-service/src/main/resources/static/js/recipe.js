class State {

    constructor() {
        this.runningARecipe = false;
        this.listeners = [];
    }

    isRunningARecipe() {
        return this.runningARecipe;
        this.notifyListeners();
    }

    startedRunningRecipe() {
        this.runningARecipe = true;
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

function runRecipe(element) {
    if (state.isRunningARecipe()) {
        return;
    }
    state.startedRunningRecipe();

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/apply-recipe",
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

function runAllRecipe() {

    if (state.isRunningARecipe()) {
        return;
    }
    state.startedRunningRecipe();

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/apply-all-recipe",
    }).always(function () {
        state.completedRunningRecipe();
    });
}


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
    'onclick="runAllRecipe()" disabled>' +
    'Run All Recipes' +
    '  <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>\n' +
    '  <span class="visually-hidden">Loading...</span>' +
    '</button>';

const runAllRecipeInNormalState = '<button type="button" class="btn btn-primary" id="recipeButton" ' +
    'name="apply-all-recipes-button" action="apply-all-recipes" ' +
    'onclick="runAllRecipe()">' +
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
            'onclick="runRecipe(this)">' +
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
