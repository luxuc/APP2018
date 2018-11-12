$('.ui.checkbox').checkbox()

$('.ui.dropdown').dropdown()

$('.ui.form.login-form')
    .form({
        fields: {
            email: {
                identifier: 'email',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'Please enter a email address'
                    },
                    {
                        type   : 'regExp[/^(([^<>()\\[\\]\\\\.,;:\\s@"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@"]+)*)|(".+"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/]',
                        prompt : 'Please enter a valid email address'
                    }
                ]
            },
            password: {
                identifier: 'password',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'Please enter a password'
                    },
                    {
                        type   : 'minLength[6]',
                        prompt : 'Your password must be at least {ruleValue} characters'
                    }
                ]
            }
        }
    })

$('.ui.form.signin-form')
    .form({
        fields: {
            username: {
                identifier: 'username',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'Please enter a username'
                    }
                ]
            },
            email: {
                identifier: 'email',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'Please enter a email address'
                    },
                    {
                        type   : 'regExp[/^(([^<>()\\[\\]\\\\.,;:\\s@"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@"]+)*)|(".+"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/]',
                        prompt : 'Please enter a valid email address'
                    }
                ]
            },
            cuisine: {
                identifier: 'preferedCuisine',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'You must select one preferred cuisine'
                    }
                ]
            },
            password: {
                identifier: 'password',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'Please enter a password'
                    },
                    {
                        type   : 'minLength[6]',
                        prompt : 'Your password must be at least 6 characters'
                    }
                ]
            },
            confirmPassword: {
                identifier: 'confirm-password',
                rules: [
                    {
                        type   : 'empty',
                        prompt : 'Please confirm your password'
                    },
                    {
                        type   : 'match[password]',
                        prompt : 'Your password does not match with confirmation'
                    }
                ]
            },
            terms: {
                identifier: 'terms',
                rules: [
                    {
                        type   : 'checked',
                        prompt : 'You must agree to the terms and conditions'
                    }
                ]
            }
        }
    })


function submitAsJson() {
    var formArray = $('form').serializeArray()
    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/users',
        data: JSON.stringify(returnArray),
        success: success,
        dataType: 'json'
    })
    function success() {
        console.log("submitted!")
    }
}

function loginAsJson() {
    var formArray = $('form').serializeArray()
    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/users/login',
        data: JSON.stringify(returnArray),
        dataType: 'json'
    }).done(function(data){
        var tokenCookie = 'auth=' + data.token
        document.cookie = tokenCookie
        console.log(login)
        // TODO jump to homepage
    })
}