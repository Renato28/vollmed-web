$('#deleteModal').on('show.bs.modal', function (e) {
    const modal = $(this);

    const id = $(e.relatedTarget).data('id');
    modal.find('input[name="id"]').val(id);

    const url = $(e.relatedTarget).data('url');
    modal.find('form').attr('action', url);
});

$('#updateModal').on('show.bs.modal', function (e) {
    const modal = $(this);

    const id = $(e.relatedTarget).data('id');
    modal.find('input[name="id"]').val(id);

    const url = $(e.relatedTarget).data('url');
    modal.find('form').attr('action', url);
});

document.addEventListener("DOMContentLoaded", function() {
    const especialidadeSelect = document.getElementById('especialidade');
    if (especialidadeSelect) {
        const medicoSelect = document.getElementById('idMedico');

        especialidadeSelect.addEventListener('change', function() {
            const especialidade = especialidadeSelect.value;

            medicoSelect.innerHTML = '<option value="">Selecione um médico</option>';

            if (especialidade) {
                fetch('/medicos/' + especialidade)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        data.forEach(medico => {
                            const option = document.createElement('option');
                            option.value = medico.id;
                            option.text = medico.nome;
                            medicoSelect.appendChild(option);
                        });
                    })
                    .catch(error => console.error('Erro:', error));
            }
        });
    }

    document.querySelectorAll('.pagination a.disabled').forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
        });
    });

    function openModal(modal) {
        modal.style.display = 'block';
    }

    function closeModal(modal) {
        modal.style.display = 'none';
    }

    function setupModalTriggers(modalId) {
        const modal = document.querySelector(modalId);

        document.querySelectorAll(`[href="${modalId}"]`).forEach(trigger => {
            trigger.addEventListener('click', function(event) {
                event.preventDefault();
                const id = this.getAttribute('data-id');
                const url = this.getAttribute('data-url');
                modal.querySelector('input[name="id"]').value = id;
                modal.querySelector('form').setAttribute('action', url);
                openModal(modal);
            });
        });

        if (modal) {
            modal.querySelectorAll('[data-dismiss="modal"]').forEach(button => {
                button.addEventListener('click', function() {
                    closeModal(modal);
                });
            });

            modal.addEventListener('click', function(event) {
                if (event.target === modal) {
                    closeModal(modal);
                }
            });
        }
    }

    setupModalTriggers('#deleteModal');
    setupModalTriggers('#updateModal');
});