function Contact(firstName, lastName, phone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.checked = false;
    this.shown = true;
}

new Vue({
    el: "#app",
    data: {
        validation: false,
        serverValidation: false,
        firstName: "",
        lastName: "",
        phone: "",
        rows: [],
        serverError: "",
        searchText: ""
    },
    methods: {
        contactToString: function (contact) {
            var note = "(";
            note += contact.firstName + ", ";
            note += contact.lastName + ", ";
            note += contact.phone;
            note += ")";
            return note;
        },
        convertContactList: function (contactListFromServer) {
            return contactListFromServer.map(function (contact, i) {
                return {
                    id: contact.id,
                    firstName: contact.firstName,
                    lastName: contact.lastName,
                    phone: contact.phone,
                    checked: false,
                    shown: true,
                    number: i + 1
                };
            });
        },
        addContact: function () {
            if (this.hasError) {
                this.validation = true;
                this.serverValidation = false;
                return;
            }

            var self = this;

            var contact = new Contact(this.firstName, this.lastName, this.phone);
            $.ajax({
                type: "POST",
                url: "/phoneBook/rpc/api/v1/addContact",
                contentType: "application/json",
                data: JSON.stringify(contact)
            }).done(function () {
                self.serverValidation = false;
            }).fail(function (ajaxRequest) {
                var contactValidation = JSON.parse(ajaxRequest.responseText);
                self.serverError = contactValidation.error;
                self.serverValidation = true;
            }).always(function () {
                self.loadData();
            });

            self.firstName = "";
            self.lastName = "";
            self.phone = "";
            self.validation = false;
        },
        loadData: function () {
            var self = this;

            $.get("/phoneBook/rpc/api/v1/getAllContacts").done(function (contactListFormServer) {
                self.rows = self.convertContactList(contactListFormServer);
            });
        },
        deleteContact: function (c) {
            var self = this;

            $.confirm({
                title: "Подтвердите удаление",
                content: "Удалить выбранный контакт?",
                buttons: {
                    ok: {
                        text: "Удалить",
                        btnClass: "btn-primary",
                        keys: ["enter"],
                        action: function () {
                            $.ajax({
                                type: "POST",
                                url: "/phoneBook/rpc/api/v1/deleteContact",
                                contentType: "application/json",
                                data: JSON.stringify([c.id])
                            }).done(function () {
                                self.loadData();
                            }).fail(function () {
                                $.alert("Ошибка на сервере");
                            })
                        }
                    },
                    cancel: {
                        text: "Отмена"
                    }
                }
            })
        },
        deleteCheckedContacts: function () {
            var checkedList = this.rows.filter(function (row) {
                return row.checked;
            }).map(function (r) {
                return r.id;
            });

            var self = this;
            $.confirm({
                title: "Подтвердите удаление",
                content: "Удалить выбранные контакты?",
                buttons: {
                    ok: {
                        text: "Удалить",
                        btnClass: "btn-primary",
                        keys: ["enter"],
                        action: function () {
                            $.ajax({
                                type: "POST",
                                url: "/phoneBook/rpc/api/v1/deleteContact",
                                contentType: "application/json",
                                data: JSON.stringify(checkedList)
                            }).done(function () {
                                    self.loadData();
                                }
                            ).fail(function () {
                                $.alert("Ошибка на сервере");
                            })
                        }
                    },
                    cancel: {
                        text: "Отмена"
                    }
                }
            })
        },
        filterContactList: function () {
            var self = this;
            $.get("/phoneBook/rpc/api/v1/getFiltered?term=" + self.searchText).done(function (filteredContactListFromServer) {
                self.rows = self.convertContactList(filteredContactListFromServer);
            });
        },
        clearFilter: function () {
            this.searchText = "";
            this.loadData();
        }
    },
    computed: {
        firstNameError: function () {
            if (this.firstName) {
                return {
                    message: "",
                    error: false
                };
            }

            return {
                message: "Поле Имя должно быть заполнено.",
                error: true
            };
        },
        lastNameError: function () {
            if (!this.lastName) {
                return {
                    message: "Поле Фамилия должно быть заполнено.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },
        phoneError: function () {
            if (!this.phone) {
                return {
                    message: "Поле Телефон должно быть заполнено.",
                    error: true
                };
            }

            var self = this;

            var sameContact = this.rows.some(function (c) {
                return c.phone === self.phone;
            });

            if (sameContact) {
                return {
                    message: "Номер телефона не должен дублировать другие номера в телефонной книге.",
                    error: true
                };
            }

            return {
                message: "",
                error: false
            };
        },
        hasError: function () {
            return this.lastNameError.error || this.firstNameError.error || this.phoneError.error;
        },
        checkedAll: {
            get: function () {
                return this.rows.length > 0 && this.rows.every(function (r) {
                    return r.checked;
                })
            },
            set: function (val) {
                this.rows.forEach(function (r) {
                    r.checked = val;
                })
            }
        }
    },
    created: function () {
        this.loadData();
    }
});

