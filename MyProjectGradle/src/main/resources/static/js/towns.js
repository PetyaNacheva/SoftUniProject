const townsList = document.getElementById('cardsList')

const allTowns= [];


fetch("http://localhost:8080/towns/api/top3")
    .then(response => response.json())
    .then(data=>{
        for (let d of data){
            allTowns.push(d);
        }
    }).then(data => displayTowns(allTowns));

const displayTowns = (towns) => {
    townsList.innerHTML = '';
    let row = [];
    for (let i = 0; i < towns.length; i++) {
        let card = document.createElement('div');
        card.classList.add('card');
        card.classList.add('my-card');
        let img = document.createElement('img');
        img.className = 'card-img-top card-image';
        img.src = towns[i].pictureUrl;
        img.alt = 'Card image cap';
        let cardBody = document.createElement('div');
        cardBody.className = 'card-body';
        let cardTitle = document.createElement('h5');
        cardTitle.className = 'card-title';
        cardTitle.innerText = towns[i].name;
        let cardText = document.createElement('p');
        cardText.className = 'card-text';
        cardText.innerText =towns[i].address;
        let a = document.createElement('a');
        a.className = 'btn btn-primary';

        a.href = '/towns/'+towns[i].id+'/details';
        a.innerText = 'Visit';
        cardBody.appendChild(cardTitle);
        cardBody.appendChild(cardText);
        cardBody.appendChild(a);
        card.appendChild(img);
        card.appendChild(cardBody);
        row.push(card);
        if ((i + 1) % 3 === 0 || towns.length - i === 1) {
            let cardGroup = document.createElement('div');
            cardGroup.classList.add('card-group');
            cardGroup.classList.add('align-content-center');
            cardGroup.classList.add('mx-auto');
            for (let j = 0; j < row.length; j++) {
                cardGroup.appendChild(row[j]);
            }
            townsList.appendChild(cardGroup);
            row = [];
        }
    }
}
