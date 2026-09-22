// BUSCA E FILTRO EM TEMPO REAL
function filtrarAlunos() {
    const termo = document.getElementById('inputBusca').value.toLowerCase().trim();
    const linhas = document.querySelectorAll('#tabelaAlunos tr');

    linhas.forEach(linha => {
        const colunas = linha.querySelectorAll('td');
        if (colunas.length > 1) {
            const nomeAluno = colunas[0].textContent.toLowerCase();
            const nomeResponsavel = colunas[3].textContent.toLowerCase();

            if (nomeAluno.includes(termo) || nomeResponsavel.includes(termo)) {
                linha.style.display = '';
            } else {
                linha.style.display = 'none';
            }
        }
    });
}

// CONTROLE DO MODAL DE ALUNOS
function abrirModal() {
    document.getElementById('formAluno').reset();
    document.getElementById('alunoIdPessoa').value = '';
    document.querySelector('.modal-header h2').innerText = 'Novo Aluno';

    const alertaErro = document.getElementById('alertaErroModal');
    if (alertaErro) {
        alertaErro.style.display = 'none';
    }

    alternarResponsavel('existente');
    document.getElementById('modalNovoAluno').classList.add('active');
}

function fecharModal() {
    document.getElementById('modalNovoAluno').classList.remove('active');
}

function editarAluno(idAluno) {
    fetch('/alunos/buscar/' + idAluno)
        .then(response => {
            if (!response.ok) throw new Error('Aluno não encontrado.');
            return response.json();
        })
        .then(aluno => {
            document.querySelector('.modal-header h2').innerText = 'Editar Aluno';
            const form = document.getElementById('formAluno');

            document.getElementById('alunoIdPessoa').value = aluno.idPessoa;
            form.querySelector('input[name="nome"]').value = aluno.nome || '';
            form.querySelector('input[name="dataDeNascimento"]').value = aluno.dataDeNascimento || '';
            form.querySelector('select[name="graduacao"]').value = aluno.graduacao || 'Branca';
            form.querySelector('input[name="tamanhoQuimono"]').value = aluno.tamanhoQuimono || '';

            const obsTextarea = form.querySelector('textarea[name="observacoesMedicas"]');
            if (obsTextarea) obsTextarea.value = aluno.observacoesMedicas || '';

            if (aluno.responsavel) {
                alternarResponsavel('existente');
                const selectResp = document.getElementById('selectResp');
                if (selectResp && aluno.responsavel.idPessoa) {
                    selectResp.value = aluno.responsavel.idPessoa;
                }

                const inputNomeResp = form.querySelector('input[name="responsavel.nome"]');
                const inputParentesco = form.querySelector('input[name="responsavel.parentesco"]');
                const inputTel = document.getElementById('inputTelefone');
                const inputCPF = document.getElementById('inputCPF');

                if (inputNomeResp) inputNomeResp.value = aluno.responsavel.nome || '';
                if (inputParentesco) inputParentesco.value = aluno.responsavel.parentesco || '';

                if (inputTel) {
                    inputTel.value = aluno.responsavel.telefone || '';
                    mascaraTelefone(inputTel);
                }
                if (inputCPF) {
                    inputCPF.value = aluno.responsavel.cpf || '';
                    mascaraCPF(inputCPF);
                }
            }

            document.getElementById('modalNovoAluno').classList.add('active');
        })
        .catch(error => console.error('Erro na requisição:', error));
}

function alternarResponsavel(tipo) {
    const boxExistente = document.getElementById('boxRespExistente');
    const boxNovo = document.getElementById('boxRespNovo');
    const selectResp = document.getElementById('selectResp');

    const inputCPF = document.getElementById('inputCPF');
    const inputTelefone = document.getElementById('inputTelefone');
    const inputNomeResp = document.querySelector('input[name="responsavel.nome"]');

    if (tipo === 'existente') {
        boxExistente.style.display = 'flex';
        boxNovo.style.display = 'none';

        if (selectResp) selectResp.required = true;

        if (inputCPF) inputCPF.required = false;
        if (inputTelefone) inputTelefone.required = false;
        if (inputNomeResp) inputNomeResp.required = false;
    } else {
        boxExistente.style.display = 'none';
        boxNovo.style.display = 'flex';

        if (selectResp) {
            selectResp.required = false;
            selectResp.value = ''; 
        }

        if (inputCPF) inputCPF.required = true;
        if (inputTelefone) inputTelefone.required = true;
        if (inputNomeResp) inputNomeResp.required = true;
    }
}

// MÁSCARAS
function mascaraTelefone(input) {
    let valor = input.value.replace(/\D/g, "");
    if (valor.length > 11) valor = valor.slice(0, 11);

    if (valor.length > 10) {
        valor = valor.replace(/^(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3");
    } else if (valor.length > 6) {
        valor = valor.replace(/^(\d{2})(\d{4})(\d{0,4})$/, "($1) $2-$3");
    } else if (valor.length > 2) {
        valor = valor.replace(/^(\d{2})(\d{0,5})$/, "($1) $2");
    } else if (valor.length > 0) {
        valor = valor.replace(/^(\d*)$/, "($1");
    }
    input.value = valor;
}

function mascaraCPF(input) {
    let valor = input.value.replace(/\D/g, "");
    if (valor.length > 11) valor = valor.slice(0, 11);

    valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
    valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
    valor = valor.replace(/(\d{3})(\d{1,2})$/, "$1-$2");

    input.value = valor;
}

// --- FUNÇÕES DE COMPETIÇÕES ---
function abrirModalCompeticoes(btn) {
    const idAluno = btn.getAttribute('data-id');
    const nomeAluno = btn.getAttribute('data-nome');

    document.getElementById('modalCompeticaoNomeTitle').innerText = 'Competições: ' + nomeAluno;
    document.getElementById('compIdAluno').value = idAluno;
    document.getElementById('modalCompeticoes').classList.add('active');
    carregarTabelaCompeticoes(idAluno);
}

function fecharModalCompeticoes() {
    document.getElementById('modalCompeticoes').classList.remove('active');
    document.getElementById('formNovaCompeticao').reset();
}

function carregarTabelaCompeticoes(idAluno) {
    const tbody = document.getElementById('tabelaCompeticoes');
    tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; color: var(--text-secondary);">Carregando...</td></tr>';

    fetch('/competicoes/aluno/' + idAluno)
        .then(res => res.json())
        .then(dados => {
            tbody.innerHTML = '';
            if (dados.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; color: var(--text-secondary);">Nenhuma competição registrada.</td></tr>';
                return;
            }

            dados.forEach(comp => {
                let corColocacao = '#F8FAFC';
                if (comp.colocacao.includes('Ouro')) corColocacao = '#FBBF24';
                else if (comp.colocacao.includes('Prata')) corColocacao = '#94A3B8';
                else if (comp.colocacao.includes('Bronze')) corColocacao = '#B45309';

                let dataFormatada = comp.dataCompeticao ? comp.dataCompeticao.split('-').reverse().join('/') : '-';

                tbody.innerHTML += `
            <tr>
                <td>
                    <div style="font-weight: 600; color: #F8FAFC;">${comp.nomeCampeonato}</div>
                    <div style="font-size: 11px; color: #64748B;">${dataFormatada}</div>
                </td>
                <td style="color: #94A3B8;">${comp.modalidade}</td>
                <td style="font-weight: 700; color: ${corColocacao};">
                    ${comp.colocacao.includes('Ouro') ? '<i class="bi bi-award-fill"></i> ' : ''}
                    ${comp.colocacao}
                </td>
            </tr>
        `;
            });
        });
}

function salvarCompeticaoViaJS() {
    const idAluno = document.getElementById('compIdAluno').value;
    const nomeCampeonato = document.getElementById('compNome').value;
    const dataCompeticao = document.getElementById('compData').value;
    const modalidade = document.getElementById('compModalidade').value;
    const colocacao = document.getElementById('compColocacao').value;

    if (!nomeCampeonato || !dataCompeticao) {
        alert("Por favor, preencha o nome do campeonato e a data.");
        return;
    }

    const payload = {
        nomeCampeonato: nomeCampeonato,
        dataCompeticao: dataCompeticao,
        modalidade: modalidade,
        colocacao: colocacao,
        idAluno: idAluno
    };

    fetch('/competicoes/salvar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (!response.ok) throw new Error('Erro ao salvar a competição');
            return response.json();
        })
        .then(data => {
            document.getElementById('compNome').value = '';
            document.getElementById('compData').value = '';
            carregarTabelaCompeticoes(idAluno);
        })
        .catch(error => {
            console.error(error);
            alert('Ocorreu um erro ao registrar a competição.');
        });
}

// --- FUNÇÕES DE DOCUMENTOS ---
function abrirModalDocumentos(btn) {
    const idAluno = btn.getAttribute('data-id');
    const nomeAluno = btn.getAttribute('data-nome');

    document.getElementById('modalDocNomeTitle').innerText = 'Documentos: ' + nomeAluno;
    document.getElementById('docIdAluno').value = idAluno;
    document.getElementById('modalDocumentos').classList.add('active');
    carregarTabelaDocumentos(idAluno);
}

function fecharModalDocumentos() {
    document.getElementById('modalDocumentos').classList.remove('active');
    document.getElementById('formUploadDocumento').reset();
}

function carregarTabelaDocumentos(idAluno) {
    const tbody = document.getElementById('tabelaDocumentos');
    tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; color: var(--text-secondary);">Carregando documentos...</td></tr>';

    fetch('/documentos/aluno/' + idAluno)
        .then(res => res.json())
        .then(dados => {
            tbody.innerHTML = '';
            if (dados.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; color: var(--text-secondary);">Nenhum documento salvo.</td></tr>';
                return;
            }

            dados.forEach(doc => {
                let icone = '<i class="bi bi-file-earmark-text" style="color: #94A3B8;"></i>';
                if (doc.nomeArquivo.toLowerCase().endsWith('.pdf')) {
                    icone = '<i class="bi bi-filetype-pdf" style="color: #EF4444;"></i>';
                } else if (doc.nomeArquivo.toLowerCase().match(/\.(jpg|jpeg|png)$/)) {
                    icone = '<i class="bi bi-file-image" style="color: #3B82F6;"></i>';
                }

                tbody.innerHTML += `
            <tr>
                <td style="font-weight: 600; color: #F8FAFC;">${doc.tipoDocumento}</td>
                <td style="color: #94A3B8;">${icone} ${doc.nomeArquivo}</td>
                <td style="text-align: right; gap: 8px;">
                    <a href="/documentos/download/${doc.idDocumento}" target="_blank" class="btn-action" title="Visualizar / Baixar" style="color: #22C55E; text-decoration: none; padding: 4px 8px;">
                        <i class="bi bi-eye"></i>
                    </a>
                    <button type="button" class="btn-action" title="Excluir" onclick="deletarDocumento(${doc.idDocumento}, ${idAluno})" style="color: #EF4444; background: transparent; border: none; cursor: pointer; padding: 4px 8px;">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `;
            });
        });
}

function fazerUploadDocumento(event) {
    event.preventDefault(); 

    const btn = document.getElementById('btnUploadDoc');
    btn.disabled = true;
    btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Salvando...';

    const form = document.getElementById('formUploadDocumento');
    const formData = new FormData(form);
    const idAluno = document.getElementById('docIdAluno').value;

    fetch('/documentos/upload', {
        method: 'POST',
        body: formData 
    })
        .then(response => {
            if (!response.ok) throw new Error('Erro no upload do arquivo');
            return response.text();
        })
        .then(mensagem => {
            form.reset(); 
            document.getElementById('docIdAluno').value = idAluno; 
            carregarTabelaDocumentos(idAluno); 
        })
        .catch(error => {
            console.error(error);
            alert('Ocorreu um erro ao salvar o documento. Verifique se o arquivo não passa de 10MB.');
        })
        .finally(() => {
            btn.disabled = false;
            btn.innerHTML = '<i class="bi bi-cloud-arrow-up-fill"></i> Salvar Documento';
        });
}

function deletarDocumento(idDocumento, idAluno) {
    if (confirm('Tem certeza que deseja excluir este documento permanentemente?')) {
        fetch('/documentos/deletar/' + idDocumento, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    carregarTabelaDocumentos(idAluno);
                } else {
                    alert('Erro ao excluir o documento.');
                }
            });
    }
}

// LISTENERS GERAIS PARA FECHAR MODAIS
document.addEventListener('DOMContentLoaded', function () {
    const modalAluno = document.getElementById('modalNovoAluno');
    if (modalAluno) {
        modalAluno.addEventListener('click', function (e) {
            if (e.target === this) fecharModal();
        });
    }

    const modalComp = document.getElementById('modalCompeticoes');
    if (modalComp) {
        modalComp.addEventListener('click', function (e) {
            if (e.target === this) fecharModalCompeticoes();
        });
    }

    const modalDoc = document.getElementById('modalDocumentos');
    if (modalDoc) {
        modalDoc.addEventListener('click', function (e) {
            if (e.target === this) fecharModalDocumentos();
        });
    }
});