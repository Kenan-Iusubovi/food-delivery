use food_delivery;

insert into product (name, price, description, available) values
            ("hamburger", 2.55, "hamburger with beef", true);
set @hamburger_id = last_insert_id();

insert into product (name, price, description, available) values
            ("chisburger", "2.95", "chisburger with beef and chis", "true");
set @chisburger_id = last_insert_id();

insert into menu(name, product_id)
values ("burgers", @hamburger_id);
values ("burgers", @chisburger_id);


insert into customer(name,surname,phone_number,email,balance,subscription) values
            ("ken","konrad","56465465","konrad@mail.com"
            ,"58.69","true");

insert into courier(name, surname, phone_number, email, license_number, years, months, days) values
            ("habib", "aliabdul","56465456","worker@mail.com"
            ,"LS-4545465-GH",10,16,26);

insert into `order`

insert into food_spot_owner(name,surname,phone_number,email, business_license) values
            ("John", "montana", "12346589",
             "owner@mail.com", "Bus-19036-KP");