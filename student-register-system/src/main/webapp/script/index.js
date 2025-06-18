// Utility function for showing alerts
function showAlert(message, type = "success") {
    const alertBox = $(`<div class="alert alert-${type}">${message}</div>`);
    $("body").prepend(alertBox);
    setTimeout(() => alertBox.fadeOut(() => alertBox.remove()), 3000);
}

// Load all students and populate the table
function loadStudents() {
    $.get("student", function (data) {
        let rows = "";
        data.forEach((s) => {
            rows += `<tr data-id="${s.id}">
                        <td contenteditable="true">${sanitize(s.name)}</td>
                        <td contenteditable="true">${sanitize(s.email)}</td>
                        <td class="passwordField">******</td> <!-- We mask passwords here -->
                        <td>
                            <button class="editBtn">Save</button>
                            <button class="delBtn">Delete</button>
                        </td>
                    </tr>`;
        });
        $("#studentTable tbody").html(rows);
    }).fail(function () {
        showAlert("Failed to load students. Please try again.", "danger");
    });
}

// Sanitize input to avoid XSS attacks
function sanitize(input) {
    return $("<div>").text(input).html();
}

// Add a new student
$("#addForm").submit(function (e) {
    e.preventDefault();

    const formData = $(this).serialize() + "&action=add";
    $.post("student", formData)
        .done(function () {
            loadStudents();
            $("#addForm")[0].reset();
            showAlert("Student added successfully!");
        })
        .fail(function () {
            showAlert("Failed to add student. Please try again.", "danger");
        });
});

// Edit an existing student
$("#studentTable").on("click", ".editBtn", function () {
    const row = $(this).closest("tr");
    const id = row.data("id");
    const name = row.find("td:eq(0)").text().trim();
    const email = row.find("td:eq(1)").text().trim();
    const password = prompt("Enter a new password for the student (Leave blank to keep the same):");

    // Validate password length if it's being updated
    if (password !== null && password !== "" && password.length < 8) {
        showAlert("Password must be at least 8 characters long.", "danger");
        return;
    }

    const payload = { action: "edit", id, name, email };
    if (password) payload.password = password;

    $.post("student", payload)
        .done(function () {
            loadStudents();
            showAlert("Student updated successfully!");
        })
        .fail(function () {
            showAlert("Failed to update student. Please try again.", "danger");
        });
});

// Delete an existing student
$("#studentTable").on("click", ".delBtn", function () {
    const id = $(this).closest("tr").data("id");

    // Confirm before deleting
    if (confirm("Are you sure you want to delete this student?")) {
        $.post("student", { action: "delete", id })
            .done(function () {
                loadStudents();
                showAlert("Student deleted successfully!");
            })
            .fail(function () {
                showAlert("Failed to delete student. Please try again.", "danger");
            });
    }
});

// Initialize when a document is ready
$(document).ready(loadStudents);