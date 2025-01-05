import axios from 'axios';
import { useNotify } from '@f3ve/vue-notify';

/**
 * Šalje HTTP zahtev ka API-ju koristeći axios.
 * 
 * @template T Tip podataka koji se očekuje kao odgovor.
 * @param {'get' | 'post' | 'patch' | 'put' | 'delete'} method HTTP metoda (GET, POST, PATCH, PUT, DELETE).
 * @param {string} url URL API krajnje tačke.
 * @param {Record<string, any>} [data={}] Podaci koji se šalju sa zahtevom (opciono).
 * @returns {Promise<T | null>} Promis koji vraća podatke tipa `T` ako je zahtev uspešan, ili `null` u slučaju greške.
 * 
 * @description U slučaju greške prilikom izvršavanja zahteva, šalje se obaveštenje korisniku sa porukom o grešci.
 */
export const requestFromApi = async <T>(
  method: 'get' | 'post' | 'patch' | 'put' | 'delete',
  url: string,
  data: Record<string, any> = {}
): Promise<T | null> => {
  const notify = useNotify();

  try {
    const response = await axios({
      method,
      url,
      data,
    });

    if (response.data.failed) {
      notify.show('Failed to perform this action', 'error'); 
      return null;
    }

    return response.data;
  } catch (error: any) {
    console.error(error);

    notify.show(error.response?.data?.code || 'An error occurred', 'error');
    return null;
  }
};