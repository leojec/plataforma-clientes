import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { ArrowLeft, Shield, Lock, Eye, FileText, CheckCircle } from 'lucide-react';

function PoliticaPrivacidade() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const handleVoltar = () => {
    if (user) {
      navigate('/');
    } else {
      navigate('/login');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto px-4 py-8">
        <button
          onClick={handleVoltar}
          className="inline-flex items-center text-blue-600 hover:text-blue-800 mb-6"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          Voltar
        </button>

        <div className="bg-white rounded-lg shadow-sm p-8">
          <div className="flex items-center mb-6">
            <Shield className="w-8 h-8 text-blue-600 mr-3" />
            <h1 className="text-3xl font-bold text-gray-900">Política de Privacidade</h1>
          </div>

          <p className="text-gray-600 mb-8">
            <strong>Última atualização:</strong> {new Date().toLocaleDateString('pt-BR', { 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}
          </p>

          <div className="prose max-w-none">
            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <Lock className="w-6 h-6 mr-2 text-blue-600" />
                1. Introdução
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                A <strong>CRM Shot Fair Brasil</strong> ("nós", "nosso" ou "empresa") está comprometida 
                com a proteção da privacidade e dos dados pessoais de nossos usuários. Esta Política de 
                Privacidade descreve como coletamos, usamos, armazenamos e protegemos suas informações 
                pessoais em conformidade com a <strong>Lei Geral de Proteção de Dados (LGPD - Lei nº 13.709/2018)</strong>.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <Eye className="w-6 h-6 mr-2 text-blue-600" />
                2. Dados Coletados
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Coletamos os seguintes tipos de dados pessoais:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li><strong>Dados de identificação:</strong> Nome, e-mail, CPF/CNPJ</li>
                <li><strong>Dados de contato:</strong> Telefone, endereço, cidade, estado</li>
                <li><strong>Dados profissionais:</strong> Cargo, empresa, área de atuação</li>
                <li><strong>Dados de navegação:</strong> Endereço IP, cookies, logs de acesso</li>
                <li><strong>Dados de uso:</strong> Histórico de interações, atividades realizadas no sistema</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <FileText className="w-6 h-6 mr-2 text-blue-600" />
                3. Finalidade do Tratamento
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Utilizamos seus dados pessoais para as seguintes finalidades:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Prestação de serviços de CRM e gestão de relacionamento com clientes</li>
                <li>Autenticação e controle de acesso ao sistema</li>
                <li>Comunicação com usuários sobre o serviço</li>
                <li>Geração de relatórios e análises de negócio</li>
                <li>Cumprimento de obrigações legais e regulatórias</li>
                <li>Melhoria contínua dos serviços oferecidos</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <Shield className="w-6 h-6 mr-2 text-blue-600" />
                4. Base Legal
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                O tratamento de dados pessoais é realizado com base nas seguintes hipóteses legais previstas na LGPD:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li><strong>Consentimento:</strong> Quando você fornece consentimento explícito</li>
                <li><strong>Execução de contrato:</strong> Para cumprimento de obrigações contratuais</li>
                <li><strong>Legítimo interesse:</strong> Para melhoria dos serviços e segurança</li>
                <li><strong>Obrigação legal:</strong> Para cumprimento de obrigações legais</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                5. Compartilhamento de Dados
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Seus dados pessoais não são vendidos, alugados ou compartilhados com terceiros, exceto nas seguintes situações:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Quando necessário para prestação dos serviços contratados</li>
                <li>Com prestadores de serviços que atuam como processadores de dados (com contratos de confidencialidade)</li>
                <li>Por determinação judicial ou de autoridades competentes</li>
                <li>Para proteção de direitos e segurança da empresa e dos usuários</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                6. Segurança dos Dados
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Implementamos medidas técnicas e organizacionais adequadas para proteger seus dados pessoais:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li>Criptografia de dados sensíveis (senhas, tokens)</li>
                <li>Controle de acesso baseado em perfis de usuário</li>
                <li>Autenticação segura com JWT (JSON Web Tokens)</li>
                <li>Backups regulares e planos de recuperação de desastres</li>
                <li>Monitoramento de segurança e detecção de anomalias</li>
                <li>Atualizações regulares de segurança</li>
              </ul>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4 flex items-center">
                <CheckCircle className="w-6 h-6 mr-2 text-blue-600" />
                7. Direitos do Titular
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Conforme a LGPD, você possui os seguintes direitos sobre seus dados pessoais:
              </p>
              <ul className="list-disc pl-6 text-gray-700 space-y-2 mb-4">
                <li><strong>Confirmação e acesso:</strong> Saber se tratamos seus dados e acessá-los</li>
                <li><strong>Correção:</strong> Solicitar correção de dados incompletos ou desatualizados</li>
                <li><strong>Anonimização, bloqueio ou eliminação:</strong> Solicitar remoção de dados desnecessários</li>
                <li><strong>Portabilidade:</strong> Receber seus dados em formato estruturado</li>
                <li><strong>Eliminação:</strong> Solicitar exclusão de dados tratados com consentimento</li>
                <li><strong>Revogação de consentimento:</strong> Retirar seu consentimento a qualquer momento</li>
                <li><strong>Informação sobre compartilhamento:</strong> Saber com quem compartilhamos seus dados</li>
              </ul>
              <p className="text-gray-700 leading-relaxed mb-4">
                Para exercer seus direitos, entre em contato através do e-mail: <strong>privacidade@crmshot.com</strong>
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                8. Retenção de Dados
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Mantemos seus dados pessoais apenas pelo tempo necessário para cumprir as finalidades 
                descritas nesta política, respeitando os prazos legais de retenção. Após o término do 
                período de retenção, os dados são eliminados de forma segura ou anonimizados.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                9. Cookies e Tecnologias Similares
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Utilizamos cookies e tecnologias similares para melhorar sua experiência no sistema, 
                autenticação e análise de uso. Você pode gerenciar suas preferências de cookies através 
                das configurações do seu navegador.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                10. Alterações nesta Política
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Podemos atualizar esta Política de Privacidade periodicamente. Notificaremos sobre 
                alterações significativas através do sistema ou por e-mail. A data da última atualização 
                está indicada no início deste documento.
              </p>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                11. Encarregado de Proteção de Dados (DPO)
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Para questões relacionadas à proteção de dados pessoais, entre em contato com nosso 
                Encarregado de Proteção de Dados:
              </p>
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-gray-700 mb-2"><strong>E-mail:</strong> dpo@crmshot.com</p>
                <p className="text-gray-700"><strong>Telefone:</strong> (47) 99999-9999</p>
              </div>
            </section>

            <section className="mb-8">
              <h2 className="text-2xl font-semibold text-gray-900 mb-4">
                12. Autoridade Nacional de Proteção de Dados (ANPD)
              </h2>
              <p className="text-gray-700 leading-relaxed mb-4">
                Caso você tenha dúvidas ou reclamações sobre o tratamento de seus dados pessoais, 
                você também pode entrar em contato com a ANPD através do site{' '}
                <a href="https://www.gov.br/anpd" target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:underline">
                  www.gov.br/anpd
                </a>.
              </p>
            </section>

            <div className="mt-8 pt-6 border-t border-gray-200">
              <p className="text-sm text-gray-500">
                Ao utilizar nossos serviços, você concorda com esta Política de Privacidade. 
                Se não concordar, por favor, não utilize nossos serviços.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PoliticaPrivacidade;

