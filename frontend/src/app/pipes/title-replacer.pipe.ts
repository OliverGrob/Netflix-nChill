import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'titleReplacer'
})
export class TitleReplacerPipe implements PipeTransform {

  transform(value: string, seriesTitle: string): string {
    const regularExpression = new RegExp(seriesTitle, 'g');

    return value.replace(regularExpression, '_______');
  }

}
