const apartmentList = document.getElementById('cardsListSearch')
const townInput = document.getElementById('town');
const typeInput = document.getElementById('type');
const searchBtn= document.getElementById('searchBtn');

const allApartments= [];
const town = document.getElementById('town').innerText;

fetch("http://localhost:8080/apartments/api/all")
    .then(response => response.json())
    .then(data=>{
        for (let d of data){
            allApartments.push(d);
        }
    });

searchBtn.addEventListener('click', (e) => {
    const townName = townInput.value;
    const type = typeInput.value;
  //.then(data => displayApartments(allApartments));
    let filterApartments =allApartments.filter(apartment => {
        if (apartment.town === townName && apartment.type === type) {
       return apartment;
        }
    });
    displayApartments(filterApartments);

});

const displayApartments = (apartments) => {
    apartmentList.innerHTML = '';
    let row = [];
    for (let i = 0; i < apartments.length; i++) {
        let card = document.createElement('div');
        card.classList.add('card');
        card.classList.add('my-card');
        card.style.width="max-width: 18rem";

        let img = document.createElement('img');
        img.className = 'card-img';
        img.src = apartments[i].picture;
        img.alt = 'Card image cap';
        img.style.width="max-width: 18rem;";
        let cardBody = document.createElement('div');
        cardBody.className = 'card-body';
        let cardTitle = document.createElement('h5');
        cardTitle.className = 'card-title';
        cardTitle.innerText = apartments[i].name;
        let cardText = document.createElement('p');
        cardText.className = 'card-text';
        cardText.innerText =apartments[i].address;
        let a = document.createElement('a');
        a.className = 'btn btn-primary';

        a.href = '/apartments/'+apartments[i].id+'/details';
        a.innerText = 'Visit';
        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);
        cardBody.appendChild(a);
        card.appendChild(img);
        card.appendChild(cardBody);
        row.push(card);


        if ((i + 1) % 3 === 0 || apartments.length - i === 1) {
            let cardGroup = document.createElement('div');
            cardGroup.classList.add('card-group');
            cardGroup.classList.add('align-content-center');
            cardGroup.classList.add('col-md-3');
            cardGroup.classList.add('mx-auto')
            cardGroup.classList.add('px-5')
            for (let j = 0; j < row.length; j++) {
                cardGroup.appendChild(row[j]);
            }
            apartmentList.appendChild(cardGroup);
            row = [];
        }
    }
}

