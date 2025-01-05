import { defineStore } from 'pinia';
import { requestFromApi } from '@/utils/api';
import Picture from '@/types/picture';
import { useNotify } from '@f3ve/vue-notify'

export const usePictureStore = defineStore('picture', {
    state: () => ({
        pictures: [] as Picture[],
        total: 0
    }),

    actions: {
        async fetchPictures(page: number, authorId?: string | null) {
            const queryParams = new URLSearchParams()
            queryParams.append('page', page.toString());
            queryParams.append('limit', '8');
            if (authorId) {
                queryParams.append('author', authorId);
            }

            const response = await requestFromApi<{ pictures: Picture[], total: number }>(
                'get',
                `pictures?${queryParams.toString()}`
            );

            if (!response) {
                return;
            }

            this.pictures = response.pictures;
            this.total = response.total;
        },

        async fetchPictureById(id: string) {
            const picture = this.pictures.find(picture => picture.picture_id === id) || null
            if (picture) {
                return picture;
            }

            const response = await requestFromApi<{ picture: Picture }>(
                'get',
                `pictures/${id}`
            )

            if (!response) {
                return null;
            }

            return response.picture;
        },

        async savePictureName(id: string, name: string) {
            const picture = await this.fetchPictureById(id);
            if (!picture) {
                return;
            }
            
            const oldName = picture.name;
            picture.name = name;

            const response = await requestFromApi<{ failed: boolean }>(
                'patch',
                `pictures/${id}`,
                { 'name': name }
            );

            if (!response) {
                picture.name = oldName; // Revert to the old name if error
                return
            }

            useNotify().show('Successfully updated name of drawing', 'success')
        },

        async deletePicture(id: string) {
            const picture = await this.fetchPictureById(id);
            if (!picture) {
                return false;
            }

            const response = await requestFromApi<{ failed: boolean }>(
                'delete',
                `pictures/${id}`,
            );

            if (!response) {
                return false;
            }

            const index = this.pictures.indexOf(picture);
            if (index !== -1) {
                this.pictures.splice(index, 1);
            }

            useNotify().show('Successfully deleted the drawing', 'success')
            return true;
        },

        async createPicture(name: string, picture_data: string[][]) {
            const response = await requestFromApi<{ picture_id: string }>(
                'post',
                'pictures/',
                {
                    'name': name,
                    'picture_data': picture_data
                }
            );

            if (!response) {
                return null;
            }

            useNotify().show('Successfully saved the drawing', 'success');
            return response.picture_id;
        },

        async savePictureData(id: string, picture_data: string[][]) {
            const picture = await this.fetchPictureById(id);
            if (!picture) {
                return;
            }

            const response = await requestFromApi<{ failed: boolean }>(
                'patch',
                `pictures/${id}`,
                { 'picture_data': picture_data }
            );

            if (!response) {
                return;
            }

            picture.picture_data = picture_data;
            useNotify().show('Successfully patched the picture data', 'success')
        }
    }
})

