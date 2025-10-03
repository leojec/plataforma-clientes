import { useOutletContext } from 'react-router-dom';

export function useSidebar() {
  const context = useOutletContext();
  return {
    sidebarExpanded: context?.sidebarExpanded ?? true
  };
}
