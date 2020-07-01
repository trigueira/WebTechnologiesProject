var rootUrl = "http://localhost:8080/learnon/rest";
var swal;

$(document).ready( function() {
            $("#workArea").hide();
            $("#login").show();

            $('#usernameLogin').keyup(function(e) {
                    $.ajax({
                        url: rootUrl + "/user/" + $('#usernameLogin').val(),
                        success: function(user) {
                            $("#refreshImage").attr("src", "resources/images/users/" + user.image);
                        },
                        error: function(result, status, xhr) {
                            $("#refreshImage").attr("src", "resources/images/users/newUser.png");
                        }
                    });
                });

            $(document).on("click", "#loginButton", function() {
                        clearErrorMessage();
                        var valid = true;
                        if ($('#usernameLogin').val() == '' || $('#usernameLogin').val() == null) {
                            $('#usernameLogin').focus();
                            $('#usernameError').show().text("*");
                            valid = false;
                        }
                        if ($('#passwordLogin').val() == '' || $('#passwordLogin').val() == null) {
                            $('#passwordLogin').focus();
                            $('#passwordError').show().text("*");
                            valid = false;
                        }
                        $('#errorMessage').show().text("Please fill the required field*");
                        if (valid) {
                            $('#errorMessage').hide();
                            loginUser();
                        }
                    });
            $('#passwordLogin').keyup(function(e) {
                if (e.which == 13) {
                    $('#loginButton').click();
                }
            });
            
            
            $(document).on("click", "#logoutButton", function() {
                    $("#refreshImage").attr("src", "resources/images/users/newUser.png");
                    reloadCourses();
                    reloadUsers();
                    cleanCourseDetails();
                    cleanUserDetails();
                    clearErrorMessage();
                    clearPasswordIndications();
                    $("#workArea").hide();
                    $("#login").show();
                });
            
                        
            $('[data-toggle="tooltip"]').tooltip();
            findNumberFreeCourse();
            findNumberPaidCourse();
            findAveragePaidCourse();
            findMostPopularSubject();
            
            findAllUsers();
            findAllCourses();
            
            
            $(document).on("click", "section #coursesTable #manageCourse span", function() {
                    findCourseById(this.id);
                });
            
            $(document).on("click", "#updateCourse", function() {
                
                if ($('#title').val() && $('#subject').val()) {
                	id = $("#courseID").text();
                	updateCourse(id);
                } else {
                	swal("Sorry!", "Course not updated. Please, try again.", "error");
                }
            });
                        
            $(document).on("click", "#deleteCourse", function() {
                id = $("#courseID").text();
                deleteCourse(id);
            });
            
            
            $(document).on("click", "#createCourse", function() {
                $("#newCourseModal").modal('show');
            });
                        
            $(document).on("click", "#saveCourse", function() {
                if ($('#newCourseTitle').val() && $('#newCourseSubject').val()) {
                	createCourse();
                } else {
                	swal("Sorry!", "Course not created. Please, try again.", "error");
                }
                
            });
            
            $(document).on("click", "#cancelCourse", function() {
                cleanCourseDetails();
            });
            
                        
            
            $(document).on("click", "#mostPopularUpdate", function() {
                $('#mostPopular h3').remove();
                findMostPopularSubject();
            });
            
            $(document).on("click", "#averagePaidCoursesUpdate", function() {
                    $('#averagePaidCourses h3').remove();
                    findAveragePaidCourse();
                });
            
            $(document).on("click", "#numberPaidCoursesUpdate", function() {
                    $('#numberPaidCourses h3').remove();
                    findNumberPaidCourse();
                });
                        
            $(document).on("click", "#numberFreeCoursesUpdate", function() {
                    $('#numberFreeCourses h3').remove();
                    findNumberFreeCourse();
                });
            
            
          
            $(document).on("click", "section #usersTable #editUser span", function() {
                    findUserById(this.id);
                });
            
            
            $(document).on("click", "#updateUser", function() {
            	 if ($('#userName').val()) {
                 	id = $("#userID").val();
                 	updateUser(id);
                 } else {
                 	swal("Sorry!", "User not updated. Please, try again.", "error");
                 }
            });
            
            
            $(document).on("click", "#deleteUser", function() {
                id = $("#userID").val();
                deleteUser(id);
            });

            
            $("#validationMessage").hide();
            $(document).on("click", "#createUser", function() {
                $("#newUserModal").modal('show');
            });
            
            $(document).on("click", "#newUserPassword", function() {
                $("#validationMessage").css({
                    display: "block"
                });
            });
            
            $(document).on("blur", "#newUserPassword", function() {
                $("#validationMessage").css({
                    display: "none"
                });
            });
                        
            $('#newUserPassword').keyup(function(e) {
                $("#validationMessage").css({
                    display: "block"
                });
                var lowerCaseLetters = /[a-z]/g;
                if ($("#newUserPassword").val().match(lowerCaseLetters)) {
                    $("#letter").attr("class", "valid");
                } else {
                    $("#letter").attr("class", "invalid");
                }
                var upperCaseLetters = /[A-Z]/g;
                if ($("#newUserPassword").val().match(upperCaseLetters)) {
                    $("#capital").attr("class", "valid");
                } else {
                    $("#capital").attr("class", "invalid");
                }
                var numbers = /[0-9]/g;
                if ($("#newUserPassword").val().match(numbers)) {
                    $("#number").attr("class", "valid");
                } else {
                    $("#number").attr("class", "invalid");
                }
                if ($("#newUserPassword").val().length >= 8) {
                    $("#length").attr("class", "valid");
                } else {
                    $("#length").attr("class", "invalid");
                }
            });

            
            $(document).on("click", "#saveUser", function() {
                var validFields = true;
                if ($('#newUserName').val() && $('#newUserPassword').val() && $('#newUserID').val()) {
                    createUser();
                } else {
                    swal("Sorry!", "User not created. Please, try again.", "error");
                }
            });

            
            $(document).on("click", "#cancelUser", function() {
                $("#newUserModal").modal('hide');
                cleanUserDetails();
                clearPasswordIndications();
            });

            
            $(document).on("click", "#aboutButton", function() {
                $("#aboutModal").modal('show');
            });
        });



// ******************** FIND USERS
var findAllUsers = function() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/user",
        dataType: "json",
        success: renderUserTable
    });
};

var renderUserTable = function(data) {
    $.each(
            data,
            function(index, user) {
                $('#userList')
                    .append(
                        '<tr><td>' +
                        user.username +
                        '</td><td>' +
                        user.name +
                        '</td><td><img src="resources/images/users/' +
                        user.image +
                        '" width="30px"></td>' +
                        '</td><td id="editUser"><span id="' +
                        user.username +
                        '" class="badge badge-info w-75 py-2">edit</span></td></tr>');
            });
    $('#usersTable').DataTable({
        "paging": true,
        "lengthMenu": [
            [4, 8],
            [4, 8]
        ]
    });
}

var reloadUsers = function() {
    $('#usersTable').DataTable().destroy();
    $("section #usersTable #userList tr").remove();
    findAllUsers();
}



// ******************** FIND COURSES
var findAllCourses = function() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/course",
        dataType: "json",
        success: renderCourseTable
    });
};

var renderCourseTable = function(data) {
    $.each(
            data,
            function(index, course) {
                $('#courseList')
                    .append(
                        '<tr><td>' +
                        course.title +
                        '</td><td><i>&euro;' +
                        course.price +
                        '</i></td><td>' +
                        course.subject +
                        '</td><td>' +
                        course.type +
                        '</td><td id="manageCourse"><span id="' +
                        course.id +
                        '" class="badge badge-info w-75 py-2">manage</span></td></tr>');
            });
    $('#coursesTable').DataTable({
        "pagingType": "simple_numbers",
        "lengthMenu": [
            [4, 8],
            [4, 8]
        ]
    });
}

var reloadCourses = function() {
    $('#coursesTable').DataTable().destroy();
    $("section #coursesTable #courseList tr").remove();
    findAllCourses();
}



// ******************** FIND SPECIFIC COURSE
var findCourseById = function(id) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/course/" + id,
        dataType: "json",
        success: fetchCourseDetails
    })
}

var fetchCourseDetails = function(data) {
    console.log('open details of course: ' + data.id);
    $("#courseDetaislModal").modal(data);
    $("#courseID").text(data.id);
    $("#coursePic").attr(
        "src",
        "resources/images/" + data.type.toLocaleLowerCase() + "/" +
        data.image);
    $("#title").val(data.title);
    $("#duration").val(data.duration);
    $("#price").val(data.price);
    $("#instructor").val(data.instructor);
    $("#subject").val(data.subject);
    $("#description").val(data.description);
    $("#type").val(data.type);
    $("#level").val(data.level);
}



// ******************** UPDATE COURSE
var updateCourse = function(id) {
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: rootUrl + "/course/" + id,
        dataType: "json",
        data: createJSON(),
        success: function(result, status, xhr) {
            refreshAllCards();
            reloadCourses();
            swal("Course Updated!", "The course was updated successfully!", "success");
        },
        error: function(result, status, xhr) {
            swal("Sorry!", "Course not updated. Try again", "error");
        }

    });
}

var createJSON = function() {
    var path = $("#coursePic")[0].src;
    var allParts = path.split("/");
    var imageName = allParts[allParts.length - 1];
    return json = JSON.stringify({
        "id": $("#courseID").text(),
        "title": $("#title").val(),
        "duration": $("#duration").val(),
        "price": $("#price").val(),
        "instructor": $("#instructor").val(),
        "subject": $("#subject").val(),
        "description": $("#description").val(),
        "type": $("#type").val(),
        "level": $("#level").val(),
        "image": imageName
    });
}



// ******************** DELETE COURSE
var deleteCourse = function(id) {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/course/" + id,
        success: function(result, status, xhr) {
            refreshAllCards();
            reloadCourses();
            swal("Course Deleted!", "The course was deleted successfully.", "success");
        },
        error: function(result, status, xhr) {
            swal("Sorry!", "Something went wrong, course not deleted.", "error");
        }
    });
}



// ******************** CREATE COURSE
var createCourse = function() {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: rootUrl + "/course",
        dataType: "json",
        data: createNewCourseJSON(),
        success: function(result, status, xhr) {
            refreshAllCards();
            reloadCourses();
            cleanCourseDetails();
            swal("Course created successfully!", "", "success");

        },
        error: function(result, status, xhr) {
            swal("Sorry!", "Course not created. Try again", "error");
        }
    });
}

var createNewCourseJSON = function() {
    return json = JSON.stringify({
        "title": $("#newCourseTitle").val(),
        "duration": $("#newCourseDuration").val(),
        "price": $("#newCoursePrice").val(),
        "instructor": $("#newCourseInstructor").val(),
        "subject": $("#newCourseSubject").val(),
        "description": $("#newCourseDescription").val(),
        "type": $("#newCourseType").val(),
        "level": $("#newCourseLevel").val(),
        "image": "newCourse.jpg"
    });
}



// ******************** CALL ALL CARDS
var refreshAllCards = function() {
    $('#mostPopular h3').remove();
    findMostPopularSubject();
    $('#averagePaidCourses h3').remove();
    findAveragePaidCourse();
    $('#numberPaidCourses h3').remove();
    findNumberPaidCourse();
    $('#numberFreeCourses h3').remove();
    findNumberFreeCourse();
}



// ******************** CARD 4
var findMostPopularSubject = function() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/course/mostPopular",
        dataType: "json",
        success: renderMostPopular
    });
};

var renderMostPopular = function(data) {
    $.each(data, function(index, string) {
        $('#mostPopular').append('<h3>' + string.toUpperCase() + '</h3>');
    });
}



// ******************** CARD 3
var findAveragePaidCourse = function() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/course/averagepaidcourses",
        dataType: "json",
        success: function(result, status, xhr) {
            $('#averagePaidCourses').append(
                '<h3>' + result.toFixed(2) + '</h3>');
        }
    });
};



// ******************** CARD 2
var findNumberPaidCourse = function() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/course/numberpaidcourses",
        dataType: "json",
        success: function(result, status, xhr) {
            $('#numberPaidCourses').append('<h3>' + result + '</h3>');
        }
    });
};



// ******************** CARD 1
var findNumberFreeCourse = function() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/course/numberfreecourses",
        dataType: "json",
        success: function(result, status, xhr) {
            $('#numberFreeCourses').append('<h3>' + result + '</h3>');
        }
    });
};



// ******************** FIND SPECIFIC USER
var findUserById = function(id) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/user/" + id,
        dataType: "json",
        success: fetchUserDetails
    })
}

var fetchUserDetails = function(data) {
    console.log('open details of user: ' + data.username);
    $("#userDetaislModal").modal(data);
    $("#userID").val(data.username);
    $("#userPic").attr("src", "resources/images/users/" + data.image);
    $("#userName").val(data.name);
}



// ******************** UPDATE USER
var updateUser = function(id) {
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: rootUrl + "/user/" + id,
        dataType: "json",
        data: createUserJSON(),
        success: function(result, status, xhr) {
            reloadUsers();
            swal("User Updated!", "The user was updated successfully!", "success");
        },
        error: function(result, status, xhr) {
            swal("Sorry!", "Something went wrong, user not updated.", "error");
        }
    });
}

var createUserJSON = function() {
    var path = $("#userPic")[0].src;
    var allParts = path.split("/");
    var imageName = allParts[allParts.length - 1];
    return json = JSON.stringify({
        "username": $("#userID").val(),
        "name": $("#userName").val(),
        "image": imageName
    });
}



// ******************** DELETE USER
var deleteUser = function(id) {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/user/" + id,
        success: function(result, status, xhr) {
            reloadUsers();
            swal("User Deleted!", "The user was deleted successfully!", "success");
        },
        error: function(result, status, xhr) {
            swal("User not deleted!", "", "error");
        }
    });
}



// ******************** CREATE USER
var createUser = function() {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: rootUrl + "/user",
        dataType: "json",
        data: createNewUserJSON(),
        success: function(result, status, xhr) {
            reloadUsers();
            cleanUserDetails();
            clearPasswordIndications();
            swal("New user added successfully!", "", "success");
        },
        error: function(result, status, xhr) {
            swal("Sorry!", "User not created. Please, try again.", "error");
        }
    });
}

var createNewUserJSON = function() {
    return json = JSON.stringify({
        "username": $("#newUserID").val(),
        "name": $("#newUserName").val(),
        "password": $("#newUserPassword").val(),
        "image": "newUser.png"
    });
}



// ******************** LOGIN
function loginUser() {
    $.ajax({
        type: "POST",
        contentType: 'application/json',
        url: rootUrl + "/user/login",
        datatype: "json",
        data: loginUserJSON(),
        success: function(data, textStatus, jqXHR) {
            sucessLogin(data);
        },
        error: function(result, status, xhr) {
            swal("Try Again!", "Username or password wrong", "warning");
        }

    });
}

var sucessLogin = function(user) {
    $('#usernameLogin').val("");
    $('#passwordLogin').val("");
    $("#login").hide();
    $("#workArea").show();
    $("#nameUserLogged").text(user[0].name);
    $("#imageUserLogged")
        .attr("src", "resources/images/users/" + user[0].image);
}

var loginUserJSON = function() {
    return JSON.stringify({
        "username": $('#usernameLogin').val(),
        "password": $('#passwordLogin').val(),
    });
};



//******************* CLEAN METHODS >> CourseDetails | UserDetails | ErrorMessages | PasswordIndications
var cleanCourseDetails = function() {
    $("#newCourseTitle").val("");
    $("#newCourseDuration").val("");
    $("#newCoursePrice").val("");
    $("#newCourseInstructor").val("");
    $("#newCourseSubject").val("");
    $("#newCourseDescription").val("");
    $('select option:nth-child(1)').prop("selected", true);
    $("#newCourseLevel").val("");
}

var cleanUserDetails = function(user) {
    $('#newUserName').val("");
    $('#newUserID').val("");
    $('#newUserPassword').val("");
}

var clearErrorMessage = function() {
    $('#usernameError').show().text("");
    $('#passwordError').show().text("");
}

var clearPasswordIndications = function(){
	$("#letter").attr("class", "invalid");
    $("#capital").attr("class", "invalid");
    $("#number").attr("class", "invalid");
    $("#length").attr("class", "invalid");
}