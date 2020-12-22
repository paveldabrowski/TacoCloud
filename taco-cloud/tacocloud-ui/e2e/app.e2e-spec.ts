import { AppPage } from './app.po';

describe('taco-web-ui App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('powinien zostać wyświetlony komunikat powitania', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Witamy w aplikacji!');
  });
});
