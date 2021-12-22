export default {

    replaceTemplate: (str: string, obj: any): string => str.replace(/\${(.*?)}/g, (x, g) => {
        let tokens: string[] = g.split(".");
        let nestedValue: any = obj;
        for (let token of tokens) {
            nestedValue = nestedValue[token];    
        }
        return nestedValue;
    }),

}