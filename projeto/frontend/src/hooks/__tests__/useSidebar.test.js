import { useSidebar } from '../useSidebar';

// Teste básico do hook
describe('useSidebar Hook', () => {
  it('deve exportar a função useSidebar', () => {
    expect(typeof useSidebar).toBe('function');
  });

  it('deve ser uma função', () => {
    expect(useSidebar).toBeDefined();
    expect(typeof useSidebar).toBe('function');
  });
});

